package com.pet_care.report_service.service.impl;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.flink.streaming.connectors.jms.JmsQueueSource;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.util.ObjectUtils;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.TextMessage;

public class SaleQueueSourceImpl extends JmsQueueSource<String> {
    private static final long serialVersionUID = 42L;

    public SaleQueueSourceImpl()
    {
        super((QueueConnectionFactory) new ActiveMQConnectionFactory("tcp://localhost:61616"), (Queue) new ActiveMQQueue("sale_report_queue"));
    }

    @Override
    protected String convert(final Message object) throws Exception
    {
        if (object instanceof TextMessage)
        {
            final TextMessage message = (TextMessage) object;
            return message.getText();
        }
        throw new MessageConversionException("Cannot convert message of type [" + ObjectUtils.nullSafeClassName(object) + "]");
    }
}
