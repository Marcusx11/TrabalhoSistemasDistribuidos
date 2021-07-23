package core;

import core.models.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestLog {
    private final List<User> listaUsersOnline;
    private final Request requisicao;
    private final Response resposta;
    private final LocalDateTime dataHoraThisLog;


    public RequestLog(List<User> listaUsersOnline,
                      Request requisicao,
                      Response resposta,
                      LocalDateTime dataHoraLog) {
        this.listaUsersOnline = listaUsersOnline;
        this.requisicao = requisicao;
        this.resposta = resposta;
        this.dataHoraThisLog = dataHoraLog;
    }

    public List<User> getListaUsersOnline() {
        return listaUsersOnline;
    }


    public Request getRequisicao() {
        return requisicao;
    }

    public Response getResposta() {
        return resposta;
    }

    public LocalDateTime getDataHoraThisLog() {
        return dataHoraThisLog;
    }

    @Override
    public String toString() {
        return "RequestLog{" +
                "listaUsersOnline=" + listaUsersOnline +
                ", requisicao=" + requisicao +
                ", resposta=" + resposta +
                ", dataHoraThisLog=" + dataHoraThisLog +
                '}';
    }
}
