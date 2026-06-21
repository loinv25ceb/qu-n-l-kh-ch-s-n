package com.hotel.server.service;

import com.hotel.server.dao.ServiceDAO;
import com.hotel.server.model.Service;
import com.hotel.server.util.LogUtil;
import java.util.List;

public class ServiceService {
    private ServiceDAO serviceDAO = new ServiceDAO();

    /**
     * Get service by ID
     */
    public Service getServiceById(int serviceId) {
        return serviceDAO.findById(serviceId);
    }

    /**
     * Get all services
     */
    public List<Service> getAllServices() {
        return serviceDAO.findAll();
    }

    /**
     * Get services by type
     */
    public List<Service> getServicesByType(String serviceType) {
        return serviceDAO.findByType(serviceType);
    }

    /**
     * Create new service
     */
    public boolean createService(Service service) {
        boolean result = serviceDAO.insert(service);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.CREATE, LogUtil.ModuleType.BOOKING,
                    "Service created: " + service.getServiceName());
        }
        return result;
    }

    /**
     * Update service
     */
    public boolean updateService(Service service) {
        boolean result = serviceDAO.update(service);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.BOOKING,
                    service.getServiceId(), "Service updated", null, null);
        }
        return result;
    }

    /**
     * Delete service
     */
    public boolean deleteService(int serviceId) {
        boolean result = serviceDAO.delete(serviceId);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.DELETE, LogUtil.ModuleType.BOOKING,
                    serviceId, "Service deleted", null, null);
        }
        return result;
    }

    /**
     * Search services
     */
    public List<Service> searchServices(String keyword) {
        return serviceDAO.search(keyword);
    }
}
