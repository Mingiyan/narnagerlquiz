package ru.halmg.narnagerl.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@Configuration
public class BotConfig {
    @Value("${BOT_TOKEN}")
    private String botToken;

    @Value("${BOT_USERNAME}")
    private String botUserName;

    @Value("${BOT_BASE_URL:}")
    private String botBaseUrl;

    @Autowired
    private CommandListener commandListener;

    @Bean
    @Profile("proxy")
    public TelegramLongPollingBot telegramLongPollingBotProxy(ProxyConfigurationProperties proxyProperties) {
        if (proxyProperties.getLogin() != null && proxyProperties.getPassword() != null) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            proxyProperties.getLogin(), proxyProperties.getPassword().toCharArray());
                }
            });
        }

        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        options.setProxyHost(proxyProperties.getHost());
        options.setProxyPort(proxyProperties.getPort());
        options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        return new GeekFactoryQuizBot(options, botToken, botUserName, commandListener);
    }

    @Bean
    @Profile("!proxy")
    public TelegramLongPollingBot telegramLongPollingBot() {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        if (botBaseUrl != null && !botBaseUrl.isEmpty()) {
            options.setBaseUrl(botBaseUrl);
        }
        return new GeekFactoryQuizBot(
                options,
                botToken, botUserName, commandListener);
    }

    @Bean
    public HashMap<CommandType, Command> activeCommandHashMap(AddTagCommand addTagCommand,
                                                              AddQuestionCommand addQuestionCommand,
                                                              StartQuizCommand startQuizCommand) {
        HashMap<CommandType, Command> activeCommandHashMap = new HashMap<>();
        activeCommandHashMap.put(CommandType.ADD_TAG, addTagCommand);
        activeCommandHashMap.put(CommandType.ADD_QUESTION, addQuestionCommand);
        activeCommandHashMap.put(CommandType.START_QUIZ, startQuizCommand);

        return activeCommandHashMap;
    }

}
