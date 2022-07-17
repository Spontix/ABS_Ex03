package dataObjects.dtoBank.dtoAccount;

import dataObjects.dtoCustomer.DTOCustomer;

import java.util.ArrayList;

public class DTOMovementsList {

    ArrayList<DTOMovement> dtoMovements=new ArrayList<>();

    public void setDTOMovements(ArrayList<DTOMovement> dtoMovements){
        this.dtoMovements=dtoMovements;
    }

    public ArrayList<DTOMovement> getDtoMovements(){
        return dtoMovements;
    }

    public DTOMovementsList(){

    }
}
