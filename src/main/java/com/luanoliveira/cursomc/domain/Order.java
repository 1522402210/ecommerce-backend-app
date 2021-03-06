package com.luanoliveira.cursomc.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="ORDERS")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@JsonFormat(pattern="dd/MM/yyyy hh:mm")
	private Date requestDate;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="order")
	private Payment payment;
	
	@ManyToOne
	@JoinColumn(name="client_id")
	private Client client;
	
	@ManyToOne
	@JoinColumn(name="shipping_address_id")
	private Address shippingAddress;
	
	@OneToMany(mappedBy="id.order")
	private Set<ItemOrder> itens = new HashSet<>();
	
	public Order() {
		
	}

	public Order(Integer id, Date requestDate, Client client, Address shippingAddress) {
		super();
		this.id = id;
		this.requestDate = requestDate;
		this.client = client;
		this.shippingAddress = shippingAddress;
	}

	public Double getTotalValue() {
		double sum = 0;
		for(ItemOrder io : itens) {
			sum = sum + io.getSubTotal();
		}
		return sum;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Set<ItemOrder> getItens() {
		return itens;
	}

	public void setItens(Set<ItemOrder> itens) {
		this.itens = itens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		
		StringBuilder builder = new StringBuilder();
		builder.append("Pedido número: ");
		builder.append(getId());
		builder.append(", Data criação: ");
		builder.append(sdf.format(getRequestDate()));
		builder.append(", Cliente: ");
		builder.append(getClient().getName());
		
		builder.append("\nEndereço de Entrega: \n");
		builder.append("\nRua: " + getShippingAddress().getAddress() + "," + getShippingAddress().getNumber());
		builder.append("\nBairro:" + getShippingAddress().getDistrict());
		builder.append("\nCidade:" + getShippingAddress().getCity().getName());
		builder.append("\nEstado:" + getShippingAddress().getCity().getState().getName());
		builder.append("\nCEP:" + getShippingAddress().getZipCode());

		builder.append("\nSituação do pagamento: ");
		builder.append(getPayment().getStatus().getDescription());
		builder.append("\nDetalhes: \n");
		for(ItemOrder io : getItens()) {
			builder.append(io.toString());
		}
		builder.append("Valor Total: ");
		builder.append(nf.format(getTotalValue()));
		return builder.toString();
	}
	
	
	
}
