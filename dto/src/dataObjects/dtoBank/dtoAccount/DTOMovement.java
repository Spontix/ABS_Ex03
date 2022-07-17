package dataObjects.dtoBank.dtoAccount;


public class DTOMovement {
    protected int toDoYazTime;
    protected int sum;
    protected String operation;
    protected int sumBeforeOperation;
    protected int sumAfterOperation;

    public DTOMovement(){

    }

    public static DTOMovement build(DTOMovement movement){

        DTOMovement dtoMovement=new DTOMovement();
        dtoMovement.toDoYazTime=movement.toDoYazTime;
        dtoMovement.sum=movement.sum;
        dtoMovement.operation=movement.operation;
        dtoMovement.sumBeforeOperation=movement.sumBeforeOperation;
        dtoMovement.sumAfterOperation=movement.sumAfterOperation;
        return dtoMovement;
    }
    public int getSum() {
        return sum;
    }

    public int getSumAfterOperation() {
        return sumAfterOperation;
    }

    public int getSumBeforeOperation() {
        return sumBeforeOperation;
    }

    public int getToDoYazTime() {
        return toDoYazTime;
    }

    public String getOperation() {
        return operation;
    }

}
