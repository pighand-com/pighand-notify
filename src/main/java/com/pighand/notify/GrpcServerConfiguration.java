package com.pighand.notify;

import com.pighand.notify.grpc.SendRpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * grpc启动类
 *
 * @author wangshuli
 */
@Slf4j
@Component
public class GrpcServerConfiguration implements CommandLineRunner {
    @Value("${server.grpc-port}")
    private int port;

    @Autowired private SendRpc sendService;

    private Server server;

    @Override
    public void run(String... args) throws Exception {
        this.start();
        this.block();
    }

    public void start() throws IOException {
        server = ServerBuilder.forPort(port).addService(sendService).build().start();

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    GrpcServerConfiguration.this.stop();
                                }));

        log.info("grpc server started, listening on " + port);
    }

    private void stop() {
        if (server != null) {
            server.shutdown();

            log.info("grpc server stop");
        }
    }

    public void block() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
