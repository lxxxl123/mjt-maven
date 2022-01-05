package com.chen.telemetry.session;

import com.chen.telemetry.HeaderClientInterceptor;
import grpc_service.GrpcServiceGrpc;
import grpc_service.GrpcServiceOuterClass.*;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.MetadataUtils;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author chenwh
 * @date 2021/12/9
 */

@Slf4j
public class Session {

    private String token ;

    private String ip;

    private String username;

    private String passowrd;

    private int port;

    private GrpcServiceGrpc.GrpcServiceBlockingStub client;

    @Builder
    public Session(String ip, String username, String passowrd, int port) {
        this.ip = ip;
        this.username = username;
        this.passowrd = passowrd;
        this.port = port;
    }



    public void connect(){
        Channel channel = ManagedChannelBuilder.forTarget(ip + ":" + port)
                .usePlaintext()
                .build();
        GrpcServiceGrpc.GrpcServiceBlockingStub client = GrpcServiceGrpc.newBlockingStub(channel);

        token = login(client, username, passowrd).getTokenId();
        log.info("login success , tokenId = {}", token);

        this.client = addHeader(channel, token);
    }

    private GrpcServiceGrpc.GrpcServiceBlockingStub addHeader(Channel channel, String tokenId){
        HeaderClientInterceptor metadata = new HeaderClientInterceptor();
        metadata.setHeader("token_id", tokenId);
        channel = ClientInterceptors.intercept(channel, metadata);
        return GrpcServiceGrpc.newBlockingStub(channel);

    }

    private LoginReply login(GrpcServiceGrpc.GrpcServiceBlockingStub client, String username, String password) {
        return client.login(LoginRequest.newBuilder()
                .setUserName(username)
                .setPassword(password)
                .build());
    }

    /**
     * 不支持
     */
    private String cli(String cmd) {
        Iterator<CliConfigReply> clis = client.cliConfig(CliConfigArgs.newBuilder().setReqId(12345)
                .setCli(cmd).build());
        return getIteratorStr(clis, e -> {
            if (e.getErrors() != null) {
                return e.getErrors() + e.getOutput();
            }
            return e.getOutput();
        });
    }


    private String display(String cmd) {
        Iterator<DisplayCmdReply> clis = client.displayCmdTextOutput(DisplayCmdArgs.newBuilder()
                .setReqId(1)
                .setCli(cmd).build());
        return getIteratorStr(clis, e -> {
            if (e.getErrors() != null) {
                return e.getErrors() + e.getOutput();
            }
            return e.getOutput();
        });
    }

    private String getReportEvent() {
        Iterator<ReportEvent> eventReport = client.getEventReport(GetReportRequest.newBuilder().setTokenId(token).build());
        return getIteratorStr(eventReport,e-> {
            log.info("", e.getJsonText());
            return e.getJsonText();
        });
    }

    private String subscribe(String name) {
        SubscribeReply subscribeReply = client.subscribeByStreamName(SubscribeRequest.newBuilder()
                .setStreamName(name).build());
        return subscribeReply.getResult();
    }



    public static <T> String getIteratorStr(Iterator<T> iterator, Function<T,String> trans) {
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(trans.apply(iterator.next()));
        }
        return sb.toString();
    }

    private void logout(){
        LogoutReply logout = client.logout(LogoutRequest.newBuilder().setTokenId(token).build());
        log.info("logout res = {}", logout.getResult());
    }

    private void close(){
        logout();
    }



    public static void main(String[] args) {
        Session session = Session.builder()
                .ip("192.168.99.254")
                .port(50051)
                .username("ipnet")
                .passowrd("admin")
                .build();

        session.connect();
        session.display("tcp");
        session.close();
    }



}
