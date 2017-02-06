package com.github.jschmidt10.fc;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.util.EnumSet;

/**
 * Configuration which creates our StateMachine and then creates the FileCombiner to use it.
 */
@Configuration
public class FileCombinerConfig {

    @Bean
    public FileCombiner fileCombiner(@Autowired StateMachine<FileCombiner.State, FileCombiner.Event> sm) {
        return new FileCombiner(sm);
    }

    @Bean
    public StateMachine<FileCombiner.State, FileCombiner.Event> stateMachine(@Autowired ListableBeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<FileCombiner.State, FileCombiner.Event> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration().beanFactory(beanFactory);

        builder.configureStates()
                .withStates().initial(FileCombiner.State.READY_FOR_DATA).states(EnumSet.allOf(FileCombiner.State.class));

        builder.configureTransitions()
                .withExternal()
                    .source(FileCombiner.State.READY_FOR_DATA).target(FileCombiner.State.CHECK_BUFFER)
                    .event(FileCombiner.Event.NEW_FILE)
                    .and()
                .withExternal()
                    .source(FileCombiner.State.CHECK_BUFFER).target(FileCombiner.State.READY_FOR_DATA)
                    .event(FileCombiner.Event.BUFFER_NOT_FULL)
                    .and()
                .withExternal()
                    .source(FileCombiner.State.CHECK_BUFFER).target(FileCombiner.State.WRITING_DATA)
                    .event(FileCombiner.Event.BUFFER_FULL)
                    .and()
                .withExternal()
                    .source(FileCombiner.State.WRITING_DATA).target(FileCombiner.State.READY_FOR_DATA)
                    .event(FileCombiner.Event.WRITE_FINISHED)
                    .and()
                .withExternal()
                    .source(FileCombiner.State.WRITING_DATA).target(FileCombiner.State.ERROR)
                    .event(FileCombiner.Event.ERROR_OCCURRED);

        return builder.build();
    }
}
