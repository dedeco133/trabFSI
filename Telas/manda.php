<?php

		
		$chatId = "403373459";
		SendMessage $manda = new SendMessage() // Create a message object object
				.setChatId(chatId)
				.setText("Bom dia!\nGostaria de saber se vc irá amanhã na consulta (manda) ");

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();

		rowInline.add(new InlineKeyboardButton().setText("Vou sim").setCallbackData("Sim"));
		rowInline.add(new InlineKeyboardButton().setText("Vai te catar, vou não").setCallbackData("Nao"));
		// Set the keyboard to the markup
		rowsInline.add(rowInline);
		// Add it to the message
		markupInline.setKeyboard(rowsInline);
		manda.setReplyMarkup(markupInline);

		try {
			execute(manda); // Manda a mensagem
		} catch (TelegramApiException c) {
			c.printStackTrace();
		}
	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onUpdateReceived(Update arg0) {

	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "516441239:AAG9VU7f04JiZ5KVSDGyzVVYxLNeon1ANJo";
	}

}
?>