package br.edu.ifsul.primeiroapp.model;

import java.util.Date;
import java.util.List;

public class Pedido {
    private Long key;
    private String formaDePagamento;
    private String estado;
    private Date dataCriacao;
    private Date dataModificacao;
    private Double totalPedido;
    private boolean situacao;
    private List<Item> itens; //associação entre as classes Pedido-ItemPedido
    private Cliente cliente; //associação entre as classes Pedido-Cliente

    public Pedido(){}

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getFormaDePagamento() {
        return formaDePagamento;
    }

    public void setFormaDePagamento(String formaDePagamento) {
        this.formaDePagamento = formaDePagamento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(Date dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public Double getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(Double totalPedido) {
        this.totalPedido = totalPedido;
    }

    public boolean isSituacao() {
        return situacao;
    }

    public void setSituacao(boolean situacao) {
        this.situacao = situacao;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "key=" + key +
                ", formaDePagamento='" + formaDePagamento + '\'' +
                ", estado='" + estado + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataModificacao=" + dataModificacao +
                ", totalPedido=" + totalPedido +
                ", situacao=" + situacao +
                ", itens=" + itens +
                ", cliente=" + cliente +
                '}';
    }
}
