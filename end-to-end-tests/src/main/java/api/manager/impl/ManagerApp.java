package api.manager.impl;

import api.RestClient;
import api.manager.lib.IManagerApp;
import exceptions.ReportFetchingException;
import lombok.Data;
import java.util.*;
import dtupay.dto.*;

@Data
public class ManagerApp implements IManagerApp {

    private RestClient api;

    public ManagerApp(RestClient api) {
        this.api = api;
    }

    public ArrayList<Payment> getPaymentsReport() throws ReportFetchingException {
        try {
            return api.get("/payments", ArrayList.class);
        } catch (Exception e) {
            throw new ReportFetchingException(e.getMessage());
        }
    }

    public ArrayList<Payment> getPaymentsReportMerchant(String id) throws ReportFetchingException {
        try {
            return api.get("/payments/merchant/" + id, ArrayList.class);
        } catch (Exception e) {
            throw new ReportFetchingException(e.getMessage());
        }
    }

    public ArrayList<Payment> getPaymentsReportCustomer(String id) throws ReportFetchingException {
        try {
            return api.get("/payments/customer/" + id, ArrayList.class);
        } catch (Exception e) {
            throw new ReportFetchingException(e.getMessage());
        }
    }

}
