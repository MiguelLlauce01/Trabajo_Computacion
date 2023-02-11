/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.unprg.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.edu.unprg.entity.Precio;
import pe.edu.unprg.entity.TipoProducto;
import pe.edu.unprg.entity.ItemPedido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import pe.edu.unprg.controller.exceptions.IllegalOrphanException;
import pe.edu.unprg.controller.exceptions.NonexistentEntityException;
import pe.edu.unprg.controller.exceptions.PreexistingEntityException;
import pe.edu.unprg.entity.Compra;
import pe.edu.unprg.entity.Producto;

/**
 *
 * @author asus
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ProductoJpaController(){
       this.emf = Persistence.createEntityManagerFactory("Trabajo_ComputacionPU"); 
    }
    
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getItemPedidoList() == null) {
            producto.setItemPedidoList(new ArrayList<ItemPedido>());
        }
        if (producto.getCompraList() == null) {
            producto.setCompraList(new ArrayList<Compra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Precio precio = producto.getPrecio();
            if (precio != null) {
                precio = em.getReference(precio.getClass(), precio.getIdPrecio());
                producto.setPrecio(precio);
            }
            TipoProducto idTipoProducto = producto.getIdTipoProducto();
            if (idTipoProducto != null) {
                idTipoProducto = em.getReference(idTipoProducto.getClass(), idTipoProducto.getIdTipoProducto());
                producto.setIdTipoProducto(idTipoProducto);
            }
            List<ItemPedido> attachedItemPedidoList = new ArrayList<ItemPedido>();
            for (ItemPedido itemPedidoListItemPedidoToAttach : producto.getItemPedidoList()) {
                itemPedidoListItemPedidoToAttach = em.getReference(itemPedidoListItemPedidoToAttach.getClass(), itemPedidoListItemPedidoToAttach.getIdItemPedido());
                attachedItemPedidoList.add(itemPedidoListItemPedidoToAttach);
            }
            producto.setItemPedidoList(attachedItemPedidoList);
            List<Compra> attachedCompraList = new ArrayList<Compra>();
            for (Compra compraListCompraToAttach : producto.getCompraList()) {
                compraListCompraToAttach = em.getReference(compraListCompraToAttach.getClass(), compraListCompraToAttach.getIdCompra());
                attachedCompraList.add(compraListCompraToAttach);
            }
            producto.setCompraList(attachedCompraList);
            em.persist(producto);
            if (precio != null) {
                precio.getProductoList().add(producto);
                precio = em.merge(precio);
            }
            if (idTipoProducto != null) {
                idTipoProducto.getProductoList().add(producto);
                idTipoProducto = em.merge(idTipoProducto);
            }
            for (ItemPedido itemPedidoListItemPedido : producto.getItemPedidoList()) {
                Producto oldProductoOfItemPedidoListItemPedido = itemPedidoListItemPedido.getProducto();
                itemPedidoListItemPedido.setProducto(producto);
                itemPedidoListItemPedido = em.merge(itemPedidoListItemPedido);
                if (oldProductoOfItemPedidoListItemPedido != null) {
                    oldProductoOfItemPedidoListItemPedido.getItemPedidoList().remove(itemPedidoListItemPedido);
                    oldProductoOfItemPedidoListItemPedido = em.merge(oldProductoOfItemPedidoListItemPedido);
                }
            }
            for (Compra compraListCompra : producto.getCompraList()) {
                Producto oldIdProductoOfCompraListCompra = compraListCompra.getIdProducto();
                compraListCompra.setIdProducto(producto);
                compraListCompra = em.merge(compraListCompra);
                if (oldIdProductoOfCompraListCompra != null) {
                    oldIdProductoOfCompraListCompra.getCompraList().remove(compraListCompra);
                    oldIdProductoOfCompraListCompra = em.merge(oldIdProductoOfCompraListCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getIdProducto()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            Precio precioOld = persistentProducto.getPrecio();
            Precio precioNew = producto.getPrecio();
            TipoProducto idTipoProductoOld = persistentProducto.getIdTipoProducto();
            TipoProducto idTipoProductoNew = producto.getIdTipoProducto();
            List<ItemPedido> itemPedidoListOld = persistentProducto.getItemPedidoList();
            List<ItemPedido> itemPedidoListNew = producto.getItemPedidoList();
            List<Compra> compraListOld = persistentProducto.getCompraList();
            List<Compra> compraListNew = producto.getCompraList();
            List<String> illegalOrphanMessages = null;
            for (ItemPedido itemPedidoListOldItemPedido : itemPedidoListOld) {
                if (!itemPedidoListNew.contains(itemPedidoListOldItemPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemPedido " + itemPedidoListOldItemPedido + " since its producto field is not nullable.");
                }
            }
            for (Compra compraListOldCompra : compraListOld) {
                if (!compraListNew.contains(compraListOldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraListOldCompra + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (precioNew != null) {
                precioNew = em.getReference(precioNew.getClass(), precioNew.getIdPrecio());
                producto.setPrecio(precioNew);
            }
            if (idTipoProductoNew != null) {
                idTipoProductoNew = em.getReference(idTipoProductoNew.getClass(), idTipoProductoNew.getIdTipoProducto());
                producto.setIdTipoProducto(idTipoProductoNew);
            }
            List<ItemPedido> attachedItemPedidoListNew = new ArrayList<ItemPedido>();
            for (ItemPedido itemPedidoListNewItemPedidoToAttach : itemPedidoListNew) {
                itemPedidoListNewItemPedidoToAttach = em.getReference(itemPedidoListNewItemPedidoToAttach.getClass(), itemPedidoListNewItemPedidoToAttach.getIdItemPedido());
                attachedItemPedidoListNew.add(itemPedidoListNewItemPedidoToAttach);
            }
            itemPedidoListNew = attachedItemPedidoListNew;
            producto.setItemPedidoList(itemPedidoListNew);
            List<Compra> attachedCompraListNew = new ArrayList<Compra>();
            for (Compra compraListNewCompraToAttach : compraListNew) {
                compraListNewCompraToAttach = em.getReference(compraListNewCompraToAttach.getClass(), compraListNewCompraToAttach.getIdCompra());
                attachedCompraListNew.add(compraListNewCompraToAttach);
            }
            compraListNew = attachedCompraListNew;
            producto.setCompraList(compraListNew);
            producto = em.merge(producto);
            if (precioOld != null && !precioOld.equals(precioNew)) {
                precioOld.getProductoList().remove(producto);
                precioOld = em.merge(precioOld);
            }
            if (precioNew != null && !precioNew.equals(precioOld)) {
                precioNew.getProductoList().add(producto);
                precioNew = em.merge(precioNew);
            }
            if (idTipoProductoOld != null && !idTipoProductoOld.equals(idTipoProductoNew)) {
                idTipoProductoOld.getProductoList().remove(producto);
                idTipoProductoOld = em.merge(idTipoProductoOld);
            }
            if (idTipoProductoNew != null && !idTipoProductoNew.equals(idTipoProductoOld)) {
                idTipoProductoNew.getProductoList().add(producto);
                idTipoProductoNew = em.merge(idTipoProductoNew);
            }
            for (ItemPedido itemPedidoListNewItemPedido : itemPedidoListNew) {
                if (!itemPedidoListOld.contains(itemPedidoListNewItemPedido)) {
                    Producto oldProductoOfItemPedidoListNewItemPedido = itemPedidoListNewItemPedido.getProducto();
                    itemPedidoListNewItemPedido.setProducto(producto);
                    itemPedidoListNewItemPedido = em.merge(itemPedidoListNewItemPedido);
                    if (oldProductoOfItemPedidoListNewItemPedido != null && !oldProductoOfItemPedidoListNewItemPedido.equals(producto)) {
                        oldProductoOfItemPedidoListNewItemPedido.getItemPedidoList().remove(itemPedidoListNewItemPedido);
                        oldProductoOfItemPedidoListNewItemPedido = em.merge(oldProductoOfItemPedidoListNewItemPedido);
                    }
                }
            }
            for (Compra compraListNewCompra : compraListNew) {
                if (!compraListOld.contains(compraListNewCompra)) {
                    Producto oldIdProductoOfCompraListNewCompra = compraListNewCompra.getIdProducto();
                    compraListNewCompra.setIdProducto(producto);
                    compraListNewCompra = em.merge(compraListNewCompra);
                    if (oldIdProductoOfCompraListNewCompra != null && !oldIdProductoOfCompraListNewCompra.equals(producto)) {
                        oldIdProductoOfCompraListNewCompra.getCompraList().remove(compraListNewCompra);
                        oldIdProductoOfCompraListNewCompra = em.merge(oldIdProductoOfCompraListNewCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ItemPedido> itemPedidoListOrphanCheck = producto.getItemPedidoList();
            for (ItemPedido itemPedidoListOrphanCheckItemPedido : itemPedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the ItemPedido " + itemPedidoListOrphanCheckItemPedido + " in its itemPedidoList field has a non-nullable producto field.");
            }
            List<Compra> compraListOrphanCheck = producto.getCompraList();
            for (Compra compraListOrphanCheckCompra : compraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Compra " + compraListOrphanCheckCompra + " in its compraList field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Precio precio = producto.getPrecio();
            if (precio != null) {
                precio.getProductoList().remove(producto);
                precio = em.merge(precio);
            }
            TipoProducto idTipoProducto = producto.getIdTipoProducto();
            if (idTipoProducto != null) {
                idTipoProducto.getProductoList().remove(producto);
                idTipoProducto = em.merge(idTipoProducto);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
