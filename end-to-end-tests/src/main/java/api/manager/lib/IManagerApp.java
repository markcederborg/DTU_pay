package api.manager.lib;

import dtupay.dto.*;
import exceptions.ReportFetchingException;

import java.util.ArrayList;

public interface IManagerApp {
    ArrayList<Payment> getPaymentsReport() throws ReportFetchingException;

    ArrayList<Payment> getPaymentsReportMerchant(String id) throws ReportFetchingException;

    ArrayList<Payment> getPaymentsReportCustomer(String id) throws ReportFetchingException;

}
