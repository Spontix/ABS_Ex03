package dataObjects.dtoBank.dtoAccount;

import dataObjects.dtoCustomer.DTOCustomer;

import java.util.ArrayList;

public class DTOCustomersList {

    ArrayList<DTOCustomer> dtoCustomers=new ArrayList<>();

    public void setDTOCustomers(ArrayList<DTOCustomer> dtoCustomers){
        this.dtoCustomers=dtoCustomers;
    }

    public ArrayList<DTOCustomer> getDTOCustomers(){
        return dtoCustomers;
    }

    public DTOCustomersList(){

    }
}
