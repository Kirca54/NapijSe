package mk.napijse.service;

import mk.napijse.model.context.AbstractEmailContext;

import javax.mail.MessagingException;

public interface MailService {
    void sendMail(final AbstractEmailContext email) throws MessagingException;
}