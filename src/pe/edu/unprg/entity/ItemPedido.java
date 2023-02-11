/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.unprg.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "item_pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemPedido.findAll", query = "SELECT i FROM ItemPedido i"),
    @NamedQuery(name = "ItemPedido.findByIdItemPedido", query = "SELECT i FROM ItemPedido i WHERE i.idItemPedido = :idItemPedido"),
    @NamedQuery(name = "ItemPedido.findByPrecio", query = "SELECT i FROM ItemPedido i WHERE i.precio = :precio"),
    @NamedQuery(name = "ItemPedido.findByCantidad", query = "SELECT i FROM ItemPedido i WHERE i.cantidad = :cantidad"),
    @NamedQuery(name = "ItemPedido.findByTotal", query = "SELECT i FROM ItemPedido i WHERE i.total = :total"),
    @NamedQuery(name = "ItemPedido.findByBorrado", query = "SELECT i FROM ItemPedido i WHERE i.borrado = :borrado")})
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_Item_Pedido")
    private Integer idItemPedido;
    @Basic(optional = false)
    @Column(name = "precio")
    private double precio;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "total")
    private double total;
    @Basic(optional = false)
    @Column(name = "borrado")
    private String borrado;
    @JoinColumn(name = "pedido", referencedColumnName = "id_Pedido")
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "producto", referencedColumnName = "id_Producto")
    @ManyToOne(optional = false)
    private Producto producto;

    public ItemPedido() {
    }

    public ItemPedido(Integer idItemPedido) {
        this.idItemPedido = idItemPedido;
    }

    public ItemPedido(Integer idItemPedido, double precio, int cantidad, double total, String borrado) {
        this.idItemPedido = idItemPedido;
        this.precio = precio;
        this.cantidad = cantidad;
        this.total = total;
        this.borrado = borrado;
    }

    public Integer getIdItemPedido() {
        return idItemPedido;
    }

    public void setIdItemPedido(Integer idItemPedido) {
        this.idItemPedido = idItemPedido;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getBorrado() {
        return borrado;
    }

    public void setBorrado(String borrado) {
        this.borrado = borrado;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idItemPedido != null ? idItemPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemPedido)) {
            return false;
        }
        ItemPedido other = (ItemPedido) object;
        if ((this.idItemPedido == null && other.idItemPedido != null) || (this.idItemPedido != null && !this.idItemPedido.equals(other.idItemPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.edu.unprg.entity.ItemPedido[ idItemPedido=" + idItemPedido + " ]";
    }
    
}
