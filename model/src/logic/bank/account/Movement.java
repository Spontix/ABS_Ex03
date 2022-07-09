package logic.bank.account;

import dataObjects.dtoBank.dtoAccount.DTOMovement;
import logic.YazLogic;

public class Movement extends DTOMovement {

    private Movement() {

    }

    public void setSum(int movementSum) {
        sum=movementSum;
    }

    public void setSumAfterOperation(int movementSumAfterOperation) {
        sumAfterOperation=movementSumAfterOperation;
    }

    public void setSumBeforeOperation(int movementSumBeforeOperation) {
        sumBeforeOperation=movementSumBeforeOperation;
    }

    public void setToDoYazTime(int movementToDoYazTime) {
        toDoYazTime=movementToDoYazTime;

    }

    public void setOperation(String movementOperation) {
        operation=movementOperation;
    }

}
