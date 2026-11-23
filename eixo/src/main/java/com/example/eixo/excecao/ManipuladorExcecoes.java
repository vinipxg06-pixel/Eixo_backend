package com.example.eixo.excecao;

import com.example.eixo.excecao.excecoespersonalizadas.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.MismatchedInputException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ManipuladorExcecoes {

    private static final Logger log = LoggerFactory.getLogger(ManipuladorExcecoes.class);

    @ExceptionHandler(CredenciaisInvalidas.class)
    public ResponseEntity<ErroPadrao> credenciaisInvalidas(CredenciaisInvalidas excecao, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErroPadrao erro = new ErroPadrao(Instant.now(), status.value(), "Credenciais inválidas", excecao.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(UsuarioInativo.class)
    public ResponseEntity<ErroPadrao> usuarioInativo(UsuarioInativo excecao, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErroPadrao erro = new ErroPadrao(Instant.now(), status.value(), "Acesso negado", excecao.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(RecursoNaoEncontrado.class)
    public ResponseEntity<ErroPadrao> recursoNaoEncontrado(RecursoNaoEncontrado recursoNaoEncontrado, HttpServletRequest request){
        String erro = "Recurso não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, recursoNaoEncontrado.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(EstoqueInsuficiente.class)
    public ResponseEntity<ErroPadrao> estoqueInsuficiente(EstoqueInsuficiente estoqueInsuficiente, HttpServletRequest request){
        String erro = "Estoque insuficiente";
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, estoqueInsuficiente.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroPadrao> illegalStateException(IllegalStateException illegalStateException, HttpServletRequest request){
        String erro = "Argumento ilegal";
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, illegalStateException.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(RecursoEmUso.class)
    public ResponseEntity<ErroPadrao> recursoEmUso(RecursoEmUso recursoEmUso, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), "Recurso em uso", recursoEmUso.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(RegistroDuplicado.class)
    public ResponseEntity<ErroPadrao> registroDuplicado(RegistroDuplicado registroDuplicado, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), "Registro duplicado", registroDuplicado.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(RegraDeNegocio.class)
    public ResponseEntity<ErroPadrao> regraDeNegocio(RegraDeNegocio regraDeNegocio, HttpServletRequest request){
        String erro = "Regra de Negócio Violada";
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, regraDeNegocio.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(TransicaoInvalida.class)
    public ResponseEntity<ErroPadrao> transicaoInvalida(TransicaoInvalida transicaoInvalida, HttpServletRequest request){
        String erro = "Transição inválida";
        HttpStatus status = HttpStatus.CONFLICT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, transicaoInvalida.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    // ---------- Erros de entrada (400) ----------

    /** Bean Validation do corpo (@Valid): devolve a lista de campos inválidos. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadrao> validacao(MethodArgumentNotValidException excecao, HttpServletRequest request) {
        List<ErroCampo> campos = excecao.getBindingResult().getFieldErrors().stream()
                .map(erro -> new ErroCampo(erro.getField(), erro.getDefaultMessage()))
                .toList();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroPadrao erro = new ErroPadrao(Instant.now(), status.value(), "Dados inválidos",
                "Revise os campos informados", request.getRequestURI(), campos);
        return ResponseEntity.status(status).body(erro);
    }

    /**
     * JSON malformado ou valor que não existe numa lista fixa.
     * Valor fora de uma lista fixa (enum, ex.: motivo do cancelamento, categoria) aponta o campo e os valores aceitos,
     * no mesmo formato das validações, para o frontend marcar o campo.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroPadrao> corpoInvalido(HttpMessageNotReadableException excecao, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MismatchedInputException valorInvalido = buscarCausa(excecao, MismatchedInputException.class);
        if (valorInvalido != null && valorInvalido.getTargetType() != null && valorInvalido.getTargetType().isEnum()) {
            String aceitos = Arrays.stream(valorInvalido.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            ErroCampo campo = new ErroCampo(caminhoDoCampo(valorInvalido), "valor inválido. Valores aceitos: " + aceitos);
            ErroPadrao erro = new ErroPadrao(Instant.now(), status.value(), "Dados inválidos",
                    "Revise os campos informados", request.getRequestURI(), List.of(campo));
            return ResponseEntity.status(status).body(erro);
        }
        ErroPadrao erro = new ErroPadrao(Instant.now(), status.value(), "Corpo da requisição inválido",
                "Verifique o formato dos dados enviados e os valores permitidos", request.getRequestURI());
        return ResponseEntity.status(status).body(erro);
    }

    /** Parâmetro da URL com tipo errado (ex.: /clientes/abc). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroPadrao> parametroInvalido(MethodArgumentTypeMismatchException excecao, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroPadrao erro = new ErroPadrao(Instant.now(), status.value(), "Parâmetro inválido",
                "Parâmetro " + excecao.getName() + " inválido", request.getRequestURI());
        return ResponseEntity.status(status).body(erro);
    }

    private static <T extends Throwable> T buscarCausa(Throwable excecao, Class<T> tipo) {
        for (Throwable causa = excecao; causa != null; causa = causa.getCause()) {
            if (tipo.isInstance(causa)) {
                return tipo.cast(causa);
            }
        }
        return null;
    }

    /** Caminho do campo no corpo, no mesmo formato do Bean Validation (ex.: pecas[0].estoqueId). */
    private static String caminhoDoCampo(JacksonException excecao) {
        StringBuilder caminho = new StringBuilder();
        for (JacksonException.Reference referencia : excecao.getPath()) {
            if (referencia.getPropertyName() != null) {
                if (!caminho.isEmpty()) caminho.append('.');
                caminho.append(referencia.getPropertyName());
            } else if (referencia.getIndex() >= 0) {
                caminho.append('[').append(referencia.getIndex()).append(']');
            }
        }
        return caminho.toString();
    }

    // ---------- Banco e erros inesperados ----------

    /** Restrição do banco que escapou das validações do código (unicidade, chave estrangeira...). */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroPadrao> integridadeDados(DataIntegrityViolationException excecao, HttpServletRequest request) {
        log.warn("Violação de integridade em {}: {}", request.getRequestURI(), excecao.getMostSpecificCause().getMessage());
        HttpStatus status = HttpStatus.CONFLICT;
        ErroPadrao erro = new ErroPadrao(Instant.now(), status.value(), "Conflito de dados",
                "Operação conflita com dados existentes", request.getRequestURI());
        return ResponseEntity.status(status).body(erro);
    }

    /**
     * Último recurso. Exceções do próprio Spring (404 de rota, 405 de método...) mantêm o status original;
     * qualquer outra vira 500 sem expor detalhes internos — o detalhe fica só no log do servidor.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadrao> erroInesperado(Exception excecao, HttpServletRequest request) {
        if (excecao instanceof ErrorResponse erroDoSpring) {
            HttpStatusCode status = erroDoSpring.getStatusCode();
            ErroPadrao erro = new ErroPadrao(Instant.now(), status.value(), "Requisição não atendida",
                    erroDoSpring.getBody().getDetail(), request.getRequestURI());
            return ResponseEntity.status(status).body(erro);
        }
        log.error("Erro inesperado em {}", request.getRequestURI(), excecao);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErroPadrao erro = new ErroPadrao(Instant.now(), status.value(), "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente", request.getRequestURI());
        return ResponseEntity.status(status).body(erro);
    }
}
