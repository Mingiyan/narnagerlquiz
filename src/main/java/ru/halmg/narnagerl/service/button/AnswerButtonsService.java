package ru.halmg.narnagerl.service.button;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.halmg.narnagerl.model.Answer;
import ru.halmg.narnagerl.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AnswerButtonsService {

    public InlineKeyboardMarkup buildQuestion(Question question) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listButtons = new ArrayList<>();
        for (Answer answer : question.getAnswers()) {
            List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton().setText(answer.getAnswer()).setCallbackData(answer.getAnswer());
            keyboardButtons.add(inlineKeyboardButton);
            listButtons.add(keyboardButtons);
        }

        Collections.shuffle(listButtons);
        inlineKeyboardMarkup.setKeyboard(listButtons);

        return inlineKeyboardMarkup;
    }
}
