package logic.bank;


import generatedEx3.AbsDescriptor;
import generatedEx3.AbsLoan;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;


public class XmlSerialization{
    //Eliran
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generatedEx3";

    public static Bank addToBank(String contentType,InputStream inputStream,Bank bank,String customerName) throws Exception {
        if(!contentType.endsWith("xml"))
            throw new Exception(" ");
        else {
            try {
                deserializeFrom(inputStream,bank,customerName);
            }
            catch (JAXBException | FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return bank;
    }

    private static void deserializeFrom(InputStream in, Bank bank,String customerName) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        AbsDescriptor absDescriptor = (AbsDescriptor) u.unmarshal(in);
        checksIfTheInformationOfTheFileAreCorrect(absDescriptor);
        absDescriptor.getAbsLoans().getAbsLoan().forEach(loan -> {
            try {
                bank.loanBuilderFileUploadServer(loan.getId(), customerName, loan.getAbsCategory(), loan.getAbsCapital(), loan.getAbsTotalYazTime(), loan.getAbsPaysEveryYaz(), loan.getAbsIntristPerPayment());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        absDescriptor.getAbsCategories().getAbsCategory().forEach(category -> bank.categories.add(category));

    }

    private static void checksIfTheInformationOfTheFileAreCorrect(AbsDescriptor absDescriptor) throws Exception {
        List<Object> objects=absDescriptor.getAbsLoans().getAbsLoan().stream().filter(l -> absDescriptor.getAbsCategories().getAbsCategory().contains(l.getAbsCategory())).collect(Collectors.toList());
        if(objects.size()!=absDescriptor.getAbsLoans().getAbsLoan().size()){
            throw new Exception("This category does not exist : " + convertToNameOfCategories(objects));
        }
        objects=absDescriptor.getAbsLoans().getAbsLoan().stream().filter(l -> l.getAbsTotalYazTime() % l.getAbsPaysEveryYaz() > 0).collect(Collectors.toList());
        if(objects.size()>0){
            throw new Exception("The division between totalYazNumber to paysEveryYaz is not an integer in : " + convertToNameOfLoans(objects));
        }
        objects=absDescriptor.getAbsLoans().getAbsLoan().stream().filter(l1->absDescriptor.getAbsLoans().getAbsLoan().stream().filter(l2->l2.getId().equals(l1.getId())).count()>1).collect(Collectors.toList());
        if(objects.size()!=0){
            throw new Exception("This client already exist : "+ convertToNameOfLoans(objects));
        }
    }

    private static String convertToNameOfLoans(List<Object> loans){
        StringBuilder string= new StringBuilder("[");
        for (Object loan:loans) {
            string.append(((AbsLoan) loan).getId()).append("-");
        }
        return string.deleteCharAt(string.lastIndexOf("-")).append("]").toString();
    }

    private static String convertToNameOfCategories(List<Object> categories){
        StringBuilder string= new StringBuilder("[");
        for (Object category:categories) {
            string.append(((AbsLoan) category).getAbsCategory()).append("-");
        }
        return string.deleteCharAt(string.lastIndexOf("-")).append("]").toString();
    }
}


