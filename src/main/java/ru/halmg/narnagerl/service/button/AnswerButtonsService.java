package ru.halmg.narnagerl.service.button;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.halmg.narnagerl.model.Question;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerButtonsService {

    public InlineKeyboardMarkup buildQuestion(Question question) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtons = question.getAnswers()
                .stream().map(answer -> new InlineKeyboardButton().setText(answer.getAnswer()).setCallbackData(answer.getAnswer()))
                .collect(Collectors.toList());
        Collections.shuffle(keyboardButtons);
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtons));

        return inlineKeyboardMarkup;
    }
}
