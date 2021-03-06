import java.util.ArrayList;
import java.sql.*;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.eclipse.swt.widgets.Synchronizer;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MandaMSG extends TelegramLongPollingBot {
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;
	String selectSql;
	String select;
	int ID = 0;
	int[] arID;
	static ArrayList<Integer> arrayid = new ArrayList<>();
	Date dt = new Date(0);
	int hr,min = 0;
	int resultado = 0;
	int insere = 0;
	String nomeCliente= null;
	String nomeProf= null;
	envioAutomatico en =  new envioAutomatico();
	FuncionaBotao fun = new FuncionaBotao();
	Conecta con = new Conecta();
	long idAuto;
	public void iniciaManual() {
		con.procProfManual();
	}
	
	public void avisaManual() {
		int i=Conecta.usuarios.size();
		ForceReplyKeyboard replyMarkup = new ForceReplyKeyboard();
		ReplyKeyboardMarkup teclado = new ReplyKeyboardMarkup();
		List<KeyboardRow> linha = new ArrayList<KeyboardRow>();
		KeyboardRow[] botao = new KeyboardRow[i];		
		String[] array = new String[i];
		
		array=Conecta.usuarios.toArray(array);

			int j =0;
			while( i != 0) {
				botao[j]= new KeyboardRow();
				botao[j].add(array[i-1]);
				linha.add(j, botao[j]);		
				j++;
				i--;
			}
			teclado.setKeyboard(linha).setOneTimeKeyboard(true);
			
			SendMessage message = new SendMessage()
					.setChatId(BotApi20.chat_id)
					.setText("Para avisarmos sobre a consulta preciso que me diga alguns dados!\n"
							+ "Primeiro preciso saber pra quem � a consulta.\n\n"
							+ "Seleciona pra mim o cliente?")
					.setReplyMarkup(teclado);
			
			BotApi20.z=10;
			System.out.println("Teclado de clientes enviado!");
		try {execute(message);} catch (TelegramApiException e) {e.printStackTrace();}
	}
	
	
	public void MandaMSG() {		
			try {
				connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/noshow?useSSL=false", "root", "");
				select = "SELECT * FROM todasconsulta WHERE NomeCliente = '"+BotApi20.nomeManual+"'";//falta variavel nome do cliente
				statement = connection.createStatement();
				resultSet = statement.executeQuery(select);
				//System.out.println("esse " + resultSet);
				System.out.println("ID: "+ID);
				
				while (resultSet.next()) {
					ID = resultSet.getInt("ID");
					nomeCliente = resultSet.getString("nomeCliente");
					nomeProf = resultSet.getString("Profissional");
					dt = resultSet.getDate("Data");
					hr = resultSet.getInt("Hora");
					min = resultSet.getInt("Minuto");
					System.out.println(dt);
					System.out.println(hr+" "+min);
					System.out.println("BAIXO " + ID);
					enviaMSG();
					ID=0;
				}
				if (ID == 0) {System.out.println("Deu nulo");}
			} catch (Exception e) {System.out.println("Ser�? "+e.getMessage());e.printStackTrace();} 
				finally{
					if (resultSet != null)try {resultSet.close();} catch (Exception e) {}
					if (statement != null)try {statement.close();} catch (Exception e) {}
					if (connection != null)try {connection.close();} catch (Exception e) {}
			}
	}// Classe
	public void enviaMSG() {
		//System.out.println("Mandou msg para: "+ID);
		long chatId = ID;
		String textinho = String.format("Eae %s, tudo bem?\n"
				+ "%s, gostaria de saber se vc ir� na consulta no dia\n*"
				+dt+"* as *"+hr+":"+min+"*", nomeCliente, nomeProf);
		
		SendMessage manda = new SendMessage() // Create a message object object
				.setChatId(chatId)
				.enableMarkdown(true)
				.setText(textinho);

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();

		rowInline.add(new InlineKeyboardButton().setText("Vou sim").setCallbackData("Sim"));
		rowInline.add(
				new InlineKeyboardButton().setText("N�o vou poder ir").setCallbackData("Nao"));
		// Set the keyboard to the markup
		rowsInline.add(rowInline);
		// Add it to the message
		markupInline.setKeyboard(rowsInline);
		manda.setReplyMarkup(markupInline);

		try {execute(manda);} catch (TelegramApiException c) {c.printStackTrace();}
	}
	
	public ResultSet ProcuraNome() {		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/noshow?useSSL=false", "root", "");
			select = String.format("SELECT PrimeiroNome from UsuariosTelegram WHERE ID=%d", idAuto); 	
			statement = connection.createStatement();
			resultSet = statement.executeQuery(select);
			resultSet.next();
			nomeCliente = resultSet.getString("PrimeiroNome");
		} catch (Exception e) {System.out.println("Ser�? "+e.getMessage());e.printStackTrace();} 
		finally{
			if (resultSet != null)try {resultSet.close();} catch (Exception e) {}
			if (statement != null)try {statement.close();} catch (Exception e) {}
			if (connection != null)try {connection.close();} catch (Exception e) {}
	}
		return resultSet;
	}
	
	public void enviaMSGAuto() {
		for (int j = en.ids.length; j > 0; j--) {
			String[] nomes = new String[j];
			idAuto = en.ids[j-1];
			ProcuraNome();
			String nome = nomeCliente;
		String textinho = String.format("Eae %s, tudo bem?\n"
				+ "O seu profissional gostaria de saber se vc ir� na consulta no amanh�", nome);
		System.out.println("Envio da mensagem automatica para: "+nome+" com o id "+idAuto);
		SendMessage manda = new SendMessage() // Create a message object object
				.setChatId(idAuto)
				.enableMarkdown(true)
				.setText(textinho);

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();

		rowInline.add(new InlineKeyboardButton().setText("Vou sim").setCallbackData("Sim"));
		rowInline.add(new InlineKeyboardButton().setText("N�o vou poder ir").setCallbackData("Nao"));
		// Set the keyboard to the markup
		rowsInline.add(rowInline);
		// Add it to the message
		markupInline.setKeyboard(rowsInline);
		manda.setReplyMarkup(markupInline);

		try {execute(manda);} catch (TelegramApiException c) {c.printStackTrace();}
		}
	}
	public void confConsulProf() {
		idAuto= en.IDUsua;
		ProcuraNome();
		long chatId = ID;
		String textinho = null;
		if (fun.confConsul==1) {
			textinho = String.format("Ol� %s!\n"
					+ "O seu cliente *%s* acabou de *confirmar* a consulta de amanh�!",en.nomeProf, nomeCliente);
		}else if(fun.confConsul==2) {
			textinho = String.format("Ol� %s!\n"
					+ "O seu cliente *%s* acabou de *desmarcar* a consulta de amanh�!",en.nomeProf, nomeCliente);
		}
		
		SendMessage manda = new SendMessage() // Create a message object object
				.setChatId(en.IDProf)
				.enableMarkdown(true)
				.setText(textinho);

		try {execute(manda);} catch (TelegramApiException c) {c.printStackTrace();}
		fun.confConsul = 0;
	}
	@Override
	public String getBotUsername() {return null;}

	@Override
	public void onUpdateReceived(Update arg0) {}

	@Override
	public String getBotToken() {return "516441239:AAG9VU7f04JiZ5KVSDGyzVVYxLNeon1ANJo";}

}
