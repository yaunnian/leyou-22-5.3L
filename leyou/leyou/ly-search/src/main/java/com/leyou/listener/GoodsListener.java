package com.leyou.listener;

import com.leyou.service.IndexService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {
    @Autowired
    private IndexService indexService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.create.index.queue",durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert","item.update"}
    ))
    public void listenCreate(Long id) throws Exception{
        if (id == null){
            return;
        }
        this.indexService.createIndex(id);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.delete.index.queue",durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = "item.delete"
    ))
    public void ListenDelete(Long id) throws Exception{
        if (id == null){
            return;
        }
        this.indexService.deleteIndex(id);
    }
}
