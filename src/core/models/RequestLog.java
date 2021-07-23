package core.models;

import core.Request;
import core.RequestCode;
import core.Response;
import core.models.transfer.Transfer;
import core.models.user.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Queue;

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
