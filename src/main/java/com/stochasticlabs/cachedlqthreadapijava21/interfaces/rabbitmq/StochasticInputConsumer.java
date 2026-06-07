package com.stochasticlabs.cachedlqthreadapijava21.interfaces.rabbitmq;

import com.stochasticlabs.cachedlqthreadapijava21.application.command.dto.StochasticInputEvent;
import com.stochasticlabs.cachedlqthreadapijava21.application.command.usecase.IngestStochasticInputUseCase;
import com.stochasticlabs.cachedlqthreadapijava21.interfaces.rabbitmq.dto.InputEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StochasticInputConsumer {

    private static final Logger log = LoggerFactory.getLogger(StochasticInputConsumer.class);

    private final IngestStochasticInputUseCase ingestUseCase;

    public StochasticInputConsumer(IngestStochasticInputUseCase ingestUseCase) {
        this.ingestUseCase = ingestUseCase;
    }

    @RabbitListener(queues = "${app.messaging.input-queue}")
    public void consume(InputEventDTO event) {
        String threadInfo = Thread.currentThread().toString();
        log.info("[rabbitmq-consumer] [{}] Data -> Original: {}, Stochastic: {}",
                threadInfo, event.originalValue(), event.stochasticValueGenerated());

        this.ingestUseCase.execute(new StochasticInputEvent(event.originalValue(), event.stochasticValueGenerated()));
    }
}
