/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.unprg.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "precio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Precio.findAll", query = "SELECT p FROM Precio p"),
    @NamedQuery(name = "Precio.findByIdPrecio", query = "SELECT p FROM Precio p WHERE p.idPrecio = :idPrecio"),
    @NamedQuery(name = "Precio.findByIdProducto", query = "SELECT p FROM Precio p WHERE p.idProducto = :idProducto"),
    @NamedQuery(name = "Precio.findByPrecioUnidad", query = "SELECT p FROM Precio p WHERE p.precioUnidad = :precioUnidad"),
    @NamedQuery(name = "Precio.findByPrecioMayor", query = "SELECT p FROM Precio p WHERE p.precioMayor = :precioMayor"),
    @NamedQuery(name = "Precio.findByFecha", query = "SELECT p FROM Precio p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Precio.findByBorrado", query = "SELECT p FROM Precio p WHERE p.borrado = :borrado")})
public class Precio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_Precio")
    private Integer idPrecio;
    @Basic(optional = false)
    @Column(name = "id_Producto")
    private int idProducto;
    @Basic(optional = false)
    @Column(name = "precio_unidad")
    private double precioUnidad;
    @Basic(optional = false)
    @Column(name = "precio_mayor")
    private double precioMayor;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "borrado")
    private String borrado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "precio")
    private List<Producto> productoList;

    public Precio() {
    }

    public Precio(Integer idPrecio) {
        this.idPrecio = idPrecio;
    }

    public Precio(Integer idPrecio, int idProducto, double precioUnidad, double precioMayor, Date fecha, String borrado) {
        this.idPrecio = idPrecio;
        this.idProducto = idProducto;
        this.precioUnidad = precioUnidad;
        this.precioMayor = precioMayor;
        this.fecha = fecha;
        this.borrado = borrado;
    }

    public Integer getIdPrecio() {
        return idPrecio;
    }

    public void setIdPrecio(Integer idPrecio) {
        this.idPrecio = idPrecio;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public double getPrecioMayor() {
        return precioMayor;
    }

    public void setPrecioMayor(double precioMayor) {
        this.precioMayor = precioMayor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getBorrado() {
        return borrado;
    }

    public void setBorrado(String borrado) {
        this.borrado = borrado;
    }

    @XmlTransient
    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrecio != null ? idPrecio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Precio)) {
            return false;
        }
        Precio other = (Precio) object;
        if ((this.idPrecio == null && other.idPrecio != null) || (this.idPrecio != null && !this.idPrecio.equals(other.idPrecio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.edu.unprg.entity.Precio[ idPrecio=" + idPrecio + " ]";
    }
    
}
