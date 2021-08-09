package com.kt.kafkatransmitter;

import com.kt.kafkatransmitter.listener.udp.ServerIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;

@SpringBootTest
class ServerRegisterTest {

    @Mock
    private IntegrationFlowContext integrationFlowContext;

    @Mock
    private ServerIterator serverIterator;

    @InjectMocks
    private ServerRegister serverRegister;

    @Test
    void testRegisterIntegrations() {
        IntegrationFlow integrationFlow = Mockito.mock(IntegrationFlow.class);
        IntegrationFlowContext.IntegrationFlowRegistrationBuilder integrationFlowRegistrationBuilder
                = Mockito.mock(IntegrationFlowContext.IntegrationFlowRegistrationBuilder.class);

        var lambdaContext = new Object() {
            int count = 0;
            int countOfCall = 0;
        };
        Mockito.when(serverIterator.hasNext()).then(invocationOnMock -> {
            if (lambdaContext.count < 2) {
                lambdaContext.count++;
                return true;
            } else {
                return false;
            }
        });
        Mockito.when(serverIterator.getNext()).then(invocationOnMock -> {
            lambdaContext.countOfCall++;
            return integrationFlow;
        });
        Mockito.when(integrationFlowContext.registration(Mockito.any())).thenReturn(integrationFlowRegistrationBuilder);
        Mockito.when(integrationFlowRegistrationBuilder.id(Mockito.anyString())).thenReturn(integrationFlowRegistrationBuilder);
        Mockito.when(integrationFlowRegistrationBuilder.register()).thenReturn(null);
        serverRegister.registerIntegrations();
        Assertions.assertEquals(2, lambdaContext.countOfCall);
    }

}