/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.home.iae;

import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author yuyurch001
 */
public class MyJPanel extends JPanel {
    FileModelDataFile fileModel;
    private File pathToSaveFolder; 
    private File pathToSave;
    private File pathToSaveIncFile;
    private File pathToSaveExpFile;


    public File getPathToSave() {
        return pathToSave;
    }
    
    public void setPathToSaveFolder(File pathToSaveFolder) {
        this.pathToSaveFolder = pathToSaveFolder;
    }
    
    public File getPathToSaveFolder() {
        return pathToSaveFolder;
    }
    
    public File getPathToSaveIncFile() {
        return pathToSaveIncFile;
    }

    public void setPathToSaveIncFile(File pathToSaveIncFile) {
        this.pathToSaveIncFile = pathToSaveIncFile;
    }

    public File getPathToSaveExpFile() {
        return pathToSaveExpFile;
    }

    public void setPathToSaveExpFile(File pathToSaveExpFile) {
        this.pathToSaveExpFile = pathToSaveExpFile;
    }
    
    
    String fileNameFrom;
    int dayIntFrom;
    int dayIntTo;
    private ListModel modelInc;
    private ListModel modelExp;
    
    
    private final String pathToTempFile="C:/IaE/Temp.txt";
    static ArrayList <String[]> linesInFile=new ArrayList();

    private void setFileModel(FileModelDataFile fileModel) {
        this.fileModel = fileModel;
    }

    public void setModelInc(ListModel modelInc) {
        this.modelInc = modelInc;
    }

    public void setModelExp(ListModel modelExp) {
        this.modelExp = modelExp;
    }
    
    public ListModel getListModel(){
        return listModel;
    }
        
    /**
     * Creates new form MyJPanel
     */
    public MyJPanel() throws IOException {
        setFileModel(new FileModelDataFile());
        createDataDir();
        initComponents();    
        setListModel(modelInc);
        restorejListItem(pathToSaveIncFile);
        setListModel(modelExp);
        restorejListItem(pathToSaveExpFile);   
    }
    
    void setPathToSave(String fileName) throws IOException{
        System.out.println("File name "+fileName);
        System.out.println("path to save folder"+pathToSaveFolder);
        
        pathToSave=new File(pathToSaveFolder, fileName+".txt").getAbsoluteFile();
        
        if (!pathToSave.exists()){
            pathToSave.createNewFile();
            System.out.println("File: "+fileName+".txt"+" is created in PATH: "+pathToSave);
        }
        System.out.println("File in  "+pathToSave+" is already exist");
    }

    public String getDate(JXDatePicker picker) {
        SimpleDateFormat formatDate=new SimpleDateFormat("dd.MM.yyyy");
        return formatDate.format(picker.getDate());
    }
    
    public String getDate(JXDatePicker picker,String format) {
        SimpleDateFormat formatDate=new SimpleDateFormat(format);
        return formatDate.format(picker.getDate());    
    }
    
    void prepareMakeStatisticsByMonth() throws FileNotFoundException, IOException {
        String fileName;
        int emptyLine=0;

        fileName=getDate(FromDate, "MM.yyyy");
        
        //set File name for parse
        setPathToSave(fileName);      
        
        //create array of string, parse each line in file
        String[] parseLine;
        
        //String for holding curient line
        String curLine="sq";
        
        FileReader reader = new FileReader(pathToSave);
        BufferedReader bufferedReader=new BufferedReader(reader); 
        
        while((curLine=bufferedReader.readLine())!=null){
            parseLine=curLine.split(" ");
            linesInFile.add(parseLine);         
        }
        
        reader.close();
        System.out.println("Complete, number of lines: "+linesInFile.size()+"  and empty line: "+emptyLine); 
        makeGeneralStatistics();
}
    
    void prepareMakeStatisticsByRange(String fromData, String toData) throws FileNotFoundException, IOException{
        //=========================Frome===============
        //get day from date fromData
        String dayFrom=fromData.substring(0, 2);
        //String to Integer
        dayIntFrom=Integer.valueOf(dayFrom);
        System.out.println("Day From is "+dayFrom);
        //get filename from fromData
        fileNameFrom=fromData.substring(3, 10);
        System.out.println("File name to start is "+fileNameFrom);
        //get month from fromData       
        int monthIntFrom=Integer.valueOf(fromData.substring(3, 5));
        System.out.println("Month From is "+monthIntFrom);
        int yearFrom=Integer.valueOf(fromData.substring(6, 10));
        
        
        //==================To=======================
        //get day from date To
        String dayTo=toData.substring(0, 2);
        dayIntTo=Integer.valueOf(dayTo);
        String fileNameTo=toData.substring(3, 10);
        System.out.println("Day To is "+dayTo);
        System.out.println("File name to end is "+fileNameTo);
        int monthIntTo=Integer.valueOf(toData.substring(3, 5));
        System.out.println("Month To is "+monthIntTo);
        int yearTo=Integer.valueOf(toData.substring(6, 10));
        
        //=================Logic=====================
        String fileName;
        int monthTemp=monthIntFrom;
        delTemp(pathToTempFile);
                      
        if (yearFrom==yearTo){           
            if(monthTemp>monthIntTo){
                JOptionPane.showMessageDialog(this, "Month Frome more than month To");
            }
            
            System.out.println("-----------------------------------------------");
            
            for (int i=monthTemp;i<monthIntTo+1;i++){
                System.out.println("i="+i);
                System.out.println("Day Int To: "+dayIntTo);
                
                    if(i<10) {
                       fileName=String.valueOf("0"+i+"."+yearFrom);
                    } else fileName=String.valueOf(i+"."+yearFrom);

                    if (monthIntFrom==monthIntTo){
                        System.out.println("(monthIntFrom==monthIntTo)");
                        if (dayIntFrom>dayIntTo){
                             JOptionPane.showMessageDialog(this, "Day Frome more than month To! Change DAY!");
                        }else
                        getListForParse(fileName, "sameDay", dayIntFrom); 
                    }else 
                
                    if ((i==monthIntFrom)&&(i!=monthIntTo)){
                    System.out.println("(i==monthIntFrom)&&(i!=monthIntTo))");
                    getListForParse(fileName, "fromDay",dayIntFrom);                 
                    }else
                                                   
                    if ((i>monthIntFrom)&&(i!=monthIntTo)){
                    System.out.println("(i>monthIntFrom)&&(i!=monthIntTo))");
                    getListForParse(fileName, "fromDay",1);                   
                    }    else
                     
                    if ((i==monthIntTo)&&(i!=monthIntFrom)){
                    System.out.println("(i==monthIntTo)&&(i!=monthIntFrom)");
                    getListForParse(fileName, "toDay",dayIntTo);                    
                    }else System.out.println("END OF IF");       
            }    
        }
        
            if (yearFrom>yearTo){ 
                JOptionPane.showMessageDialog(this, "Year Frome more than year To");
            }
    }
    
    void getListForParse() throws IOException{       
        String curLine;
        int emptyLine=0;
        
        //set File name for parse
        setPathToSave(fileNameFrom);
        
        //create ArrayList for holding the line of File
        linesInFile=new ArrayList();
        
        //create array of string, parse each line in file
        String[] parseLine;
         
        
        FileReader reader = new FileReader(getPathToSave());
        BufferedReader bufferedReader=new BufferedReader(reader); 
        
        while((curLine=bufferedReader.readLine())!=null){
            if ((Integer.valueOf(curLine.substring(0, 2)))>=dayIntFrom){
                System.out.println(curLine);
                parseLine=curLine.split(" ");
                linesInFile.add(parseLine);
            }
        }
        reader.close();
        System.out.println("Complete, number of lines: "+linesInFile.size()+"  and empty line: "+emptyLine);
    }
    
    void getListForParse(String filename, String derection, int day) throws FileNotFoundException, IOException{
        String curLine;
        int emptyLine=0;
               
        //set File name for parse
        setPathToSave(filename);
        
        FileReader reader = new FileReader(getPathToSave());
        BufferedReader bufferedReader=new BufferedReader(reader); 
        
        switch (derection){
            case "fromDay":
                System.out.println("fromDay");
               
                while((curLine=bufferedReader.readLine())!=null){   
                    if ((Integer.valueOf(curLine.substring(0, 2)))>=dayIntFrom){
                        System.out.println(curLine);
                        linesInFile.add(curLine.split(" "));}
                    }
                reader.close(); 
                break;
                     
            case "toDay":
                System.out.println("toDay");
                
                while((curLine=bufferedReader.readLine())!=null){   
                    if ((Integer.valueOf(curLine.substring(0, 2)))<=dayIntTo){
                        System.out.println(curLine);
                        
                        linesInFile.add(curLine.split(" "));}
                    }
                    reader.close(); 
                    break;
                    
            case "sameDay":
                System.out.println("SameDay");
                
                while((curLine=bufferedReader.readLine())!=null){   
                    if (((Integer.valueOf(curLine.substring(0, 2)))>=dayIntFrom)&&(Integer.valueOf(curLine.substring(0, 2)))<=dayIntTo){
                        System.out.println(curLine);
                        
                        linesInFile.add(curLine.split(" "));}
                    }
                    reader.close(); 
                    break;             
        }
        
    }
        
    void delTemp(String path) throws IOException{
        Path pathIs=FileSystems.getDefault().getPath(pathToTempFile);
        Files.deleteIfExists(pathIs);
        System.out.println("\n File "+pathIs.getFileName()+" is deleted!-------------- \n");
    }
        
    void makeGeneralStatistics(){
        String monthFromStatistic=null;
        String monthToStatistic=null;
        if (byMonth.isSelected()){       
            monthFromStatistic=getDate(FromDate, "dd.MM.yyyy");
        }else if (byRange.isSelected()){       
            monthFromStatistic=getDate(FromDate, "dd.MM.yyyy");
            monthToStatistic=getDate(ToDate, "dd.MM.yyyy");  
        }
        int sumOfIncome=0;
        int sumOfExpense=0;

        for (String[] stt:linesInFile){
           if (stt[ItemOfDB.DERECTION].equals("+")){
               sumOfIncome=sumOfIncome+(Integer.parseInt(stt[ItemOfDB.SUM]));
           }else sumOfExpense=sumOfExpense+(Integer.parseInt(stt[ItemOfDB.SUM]));
        }
       
       //Output in console --------------------------------------------------
        System.out.println("Summa of Income: "+sumOfIncome);
        System.out.println("Summa of Expense: "+sumOfExpense);
        int freeMoney=sumOfIncome-sumOfExpense;
        System.out.println("Free money: "+(sumOfIncome-sumOfExpense));
        System.out.println("============================");
            if (freeMoney<0){String whereIs="Where is MONEY???";
            System.out.print(whereIs);}
        
        //Output in StatisticArea
        if (byMonth.isSelected()){
        StatisticArea.setText("Statistic of the: "+monthFromStatistic+"\n"+"Summa of Income: "+sumOfIncome+"\n"+"Summa of Expense: "+sumOfExpense+"\n"+"Free money:" +(sumOfIncome-sumOfExpense)+"\n");
        }else if (byRange.isSelected()){
            StatisticArea.setText("Statistic from "+monthFromStatistic+" to "+monthToStatistic+"\n"+"Summa of Income: "+sumOfIncome+"\n"+"Summa of Expense: "+sumOfExpense+"\n"+"Free money:" +(sumOfIncome-sumOfExpense)+"\n");
        }
        if (freeMoney<0){String whereIs="Where is MONEY???";
            System.out.print(whereIs);
            StatisticArea.append(whereIs);}
    }
      
    void getMonthStatisticByAllItem(){
        //Create HashSet for handling unique item from repeating data
        HashSet<String> item=new HashSet<>();    
        String nextItem;
        int sum=0;
        String massage;
        
        
        //Add Expense Item to HashSet 
        for(String[] str: linesInFile){
            if (str[ItemOfDB.DERECTION].equals("-"))
                item.add(str[ItemOfDB.ITEM]);
        }
        
        Iterator<String> iterator=item.iterator();
        
        //Output statistic  
        massage="Expense:\n";
        System.out.println("Expense:\n");
         
        while(iterator.hasNext()){
            nextItem=iterator.next();

           
            for (String[] str: linesInFile){
                if ((str[ItemOfDB.DERECTION].equals("-"))&&str[ItemOfDB.ITEM].equals(nextItem)){
                   sum=sum+Integer.parseInt(str[ItemOfDB.SUM]);
                }      
            }
            massage+=nextItem+": "+sum+"\n";          
            System.out.print(nextItem+": ");
            System.out.println(sum);
            sum=0;
        }
        item.clear();
        //=============================================================
        //Add Income Item to HashSet 
        for(String[] str: linesInFile){
            if (str[ItemOfDB.DERECTION].equals("+"))
                item.add(str[ItemOfDB.ITEM]);
        }
        
        iterator=item.iterator();
              
        //Output statistic     
        massage+="==========================\n";
        massage+="Income: \n";
        System.out.println("Income:");
        while(iterator.hasNext()){
            nextItem=iterator.next();
            
           
            for (String[] str: linesInFile){
                if ((str[ItemOfDB.DERECTION].equals("+"))&&str[ItemOfDB.ITEM].equals(nextItem)){
                   sum=sum+Integer.parseInt(str[ItemOfDB.SUM]);
                }   
            }
            massage+=nextItem+": "+sum+"\n";
            System.out.print(nextItem+": ");
            System.out.println(sum);
            sum=0;
        }
        item.clear();
        StatisticArea.setText(massage);
    }
    
    //get data frome tab "Input"
    ArrayList<String> getDataFromeInput(){
        ArrayList<String> dataInput=new ArrayList<>(10);
        
        //add element index[0]:date
        if (payDate.getDate()!=null){
        dataInput.add(0,getDate(payDate,"dd.MM.yyyy"));
        }else {
            JOptionPane.showMessageDialog(this, "Input date!");
            return dataInput=null; // get data faild!
        }
        
        //add element index[1]:income or expense;
        if (incomeRadioButton.isSelected()){
            dataInput.add(1,"+");}
        else dataInput.add(1, "-");
          
        //add element index[2]:sum;
        if(cashtField.getText().isEmpty()|cashtField.getText().equalsIgnoreCase("0")){
            JOptionPane.showMessageDialog(this, "Where is cash???");
            return dataInput=null;
        }else dataInput.add(2,cashtField.getText());
               
        //add element index[3]:Item;
        if(itemField.getSelectedItem()!=null)
           dataInput.add(3,itemField.getSelectedItem().toString());
        else {JOptionPane.showMessageDialog(this, "Input item in Edit tab!!!");
        return dataInput=null;
        }
        
        //add element index[4]:Description;
        if (addDesctiption.isSelected())
            dataInput.add(4,descriptionArea.getText());
        else dataInput.add(4,"");
        return dataInput;
    }
     


            
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        addDesctiption = new javax.swing.JCheckBox();
        addToBase = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        descriptionArea = new javax.swing.JTextArea();
        itemField = new javax.swing.JComboBox<>();
        dateLable = new java.awt.Label();
        cashLable = new java.awt.Label();
        itemLabel = new java.awt.Label();
        payDate = new org.jdesktop.swingx.JXDatePicker();
        incomeRadioButton = new javax.swing.JRadioButton();
        expenseRadioButton = new javax.swing.JRadioButton();
        cashtField = new javax.swing.JFormattedTextField();
        dateLable1 = new java.awt.Label();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListForIncoming = new javax.swing.JList<>();
        incAddButton = new javax.swing.JButton();
        incDelButton = new javax.swing.JButton();
        incItemTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        expItemTextField = new javax.swing.JTextField();
        expAddButton1 = new javax.swing.JButton();
        expDelButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListForExpens = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        FromDate = new org.jdesktop.swingx.JXDatePicker();
        ToDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        getStatisticButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        StatisticArea = new javax.swing.JTextArea();
        byMonth = new javax.swing.JRadioButton();
        byRange = new javax.swing.JRadioButton();
        generalChack = new javax.swing.JCheckBox();
        itemCheck = new javax.swing.JCheckBox();

        jMenu2.setText("File");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Edit");
        jMenuBar1.add(jMenu3);

        setBackground(new java.awt.Color(153, 153, 153));
        setToolTipText("");

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        addDesctiption.setText("Добавить описание");
        addDesctiption.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                addDesctiptionItemStateChanged(evt);
            }
        });

        addToBase.setText("Добавить");
        addToBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToBaseActionPerformed(evt);
            }
        });

        descriptionArea.setColumns(20);
        descriptionArea.setRows(5);
        descriptionArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, addDesctiption, org.jdesktop.beansbinding.ELProperty.create("${selected}"), descriptionArea, org.jdesktop.beansbinding.BeanProperty.create("editable"));
        bindingGroup.addBinding(binding);

        jScrollPane2.setViewportView(descriptionArea);
        descriptionArea.setVisible(false);

        itemField.setModel(model);
        itemField.setToolTipText("Item");
        itemField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemFieldActionPerformed(evt);
            }
        });

        dateLable.setText("Когда");

        cashLable.setText("Сколько");

        itemLabel.setText("На что?");

        payDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payDateActionPerformed(evt);
            }
        });
        payDate.setFormats("dd.MM.yyyy");
        payDate.setDate(getCurDate());

        incomeRadioButton.setText("Доходы");
        incomeRadioButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        incomeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incomeRadioButtonActionPerformed(evt);
            }
        });

        expenseRadioButton.setText("Расходы");
        expenseRadioButton.setSelected(true);
        expenseRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expenseRadioButtonActionPerformed(evt);
            }
        });

        cashtField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####.###"))));
        cashtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashtFieldActionPerformed(evt);
            }
        });

        dateLable1.setText("руб.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(dateLable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cashLable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(itemLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cashtField, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(addToBase, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(itemField, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(payDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateLable1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(addDesctiption)
                        .addGap(8, 8, 8)
                        .addComponent(incomeRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(expenseRadioButton)))
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateLable, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(payDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cashtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cashLable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateLable1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addToBase)
                    .addComponent(addDesctiption)
                    .addComponent(incomeRadioButton)
                    .addComponent(expenseRadioButton))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Добавить", jPanel1);

        jListForIncoming.setModel(new DefaultListModel());
        modelInc=jListForIncoming.getModel();
        jScrollPane1.setViewportView(jListForIncoming);

        incAddButton.setText("Add");
        incAddButton.setActionCommand("Income");
        incAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incAddButtonActionPerformed(evt);
            }
        });

        incDelButton.setText("Delete");
        incDelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incDelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Доходы");

        expAddButton1.setText("Add");
        expAddButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expAddButton1ActionPerformed(evt);
            }
        });

        expDelButton1.setText("Delete");
        expDelButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expDelButton1ActionPerformed(evt);
            }
        });

        jListForExpens.setModel(new DefaultListModel());
        modelExp=jListForExpens.getModel();
        jScrollPane3.setViewportView(jListForExpens);

        jLabel2.setText("Расходы");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(incAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(incItemTextField)
                            .addComponent(incDelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(66, 66, 66)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(expAddButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(expItemTextField)
                            .addComponent(expDelButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(expItemTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expAddButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(expDelButton1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(incItemTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(incAddButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(incDelButton))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(120, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Редактировать", jPanel2);

        FromDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FromDateActionPerformed(evt);
            }
        });
        FromDate.setFormats("dd.MM.yyyy");
        FromDate.setDate(getCurDate());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, byMonth, org.jdesktop.beansbinding.ELProperty.create("${!selected}"), ToDate, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jLabel3.setText("От");

        jLabel4.setText("To");

        getStatisticButton.setText("Запросить");
        getStatisticButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getStatisticButtonActionPerformed(evt);
            }
        });

        StatisticArea.setColumns(20);
        StatisticArea.setRows(5);
        jScrollPane5.setViewportView(StatisticArea);

        byMonth.setText("За месяц");
        byMonth.setSelected(true);
        byMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                byMonthActionPerformed(evt);
            }
        });

        byRange.setText("За промежуток премяни");
        byRange.setActionCommand("За промежуток времяни");
        byRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                byRangeActionPerformed(evt);
            }
        });

        generalChack.setSelected(true);
        generalChack.setText("Общая");
        generalChack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generalChackActionPerformed(evt);
            }
        });

        itemCheck.setText("Подробная");
        itemCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(byMonth)
                        .addGap(18, 18, 18)
                        .addComponent(byRange))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(FromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(getStatisticButton)
                            .addComponent(generalChack)
                            .addComponent(itemCheck))))
                .addContainerGap(175, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(getStatisticButton))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(byMonth)
                    .addComponent(byRange))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(generalChack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemCheck))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Статистика", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Редактировать");

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void addDesctiptionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_addDesctiptionItemStateChanged
        if (evt.getStateChange()==ItemEvent.SELECTED){
            descriptionArea.setVisible(true);
        }else descriptionArea.setVisible(false);
    }//GEN-LAST:event_addDesctiptionItemStateChanged

    private void itemFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemFieldActionPerformed

    private void payDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_payDateActionPerformed

    private void incomeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incomeRadioButtonActionPerformed
        expenseRadioButton.setSelected(false); 
        if (!incomeRadioButton.isSelected()&!expenseRadioButton.isSelected()) incomeRadioButton.setSelected(true);
        putElementToComboBox(pathToSaveIncFile, (DefaultComboBoxModel<String>)itemField.getModel());
        
    }//GEN-LAST:event_incomeRadioButtonActionPerformed

    private void expenseRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expenseRadioButtonActionPerformed
        incomeRadioButton.setSelected(false);
        if (!incomeRadioButton.isSelected()&!expenseRadioButton.isSelected()) expenseRadioButton.setSelected(true);
        putElementToComboBox(pathToSaveExpFile, (DefaultComboBoxModel<String>)itemField.getModel());
// TODO add your handling code here:
    }//GEN-LAST:event_expenseRadioButtonActionPerformed

    private void addToBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToBaseActionPerformed
        // TODO add your handling code here:
        addToStorage(getDataFromeInput());
    }//GEN-LAST:event_addToBaseActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if(jTabbedPane1.getSelectedIndex()==0){
            if (expenseRadioButton.isSelected()) putElementToComboBox(pathToSaveExpFile, model);
            else putElementToComboBox(pathToSaveIncFile, model);
        }
        
             // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void incAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incAddButtonActionPerformed
            incItemTextField.requestFocus();
            if (incItemTextField.getText().isEmpty()){
                System.out.println("No Item found, please enter the item name.");
            }else{
                setListModel(modelInc);
                addElementToItemList(incItemTextField.getText(),pathToSaveIncFile); 
                incItemTextField.setText("");       
            }         
    }//GEN-LAST:event_incAddButtonActionPerformed

    private void expDelButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expDelButton1ActionPerformed
        if (jListForExpens.getSelectedValue()==null){
            System.out.println("Select item for deleting!");
        }else {
            setListModel(modelExp);
            delElementFromItemList(jListForExpens.getSelectedIndex(), pathToSaveExpFile);
    }//GEN-LAST:event_expDelButton1ActionPerformed
    }
    private void incDelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incDelButtonActionPerformed
        if (jListForIncoming.getSelectedValue()==null){
            System.out.println("Select item for deleting!");
        }else {
            setListModel(modelInc);
            delElementFromItemList(jListForIncoming.getSelectedIndex(), pathToSaveIncFile);
        }
    }//GEN-LAST:event_incDelButtonActionPerformed

    private void expAddButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expAddButton1ActionPerformed
            expItemTextField.requestFocus();
            if (expItemTextField.getText().isEmpty()){
                System.out.println("No Item found, please enter the item name.");
            }else{
                setListModel(modelExp);
                addElementToItemList(expItemTextField.getText(),pathToSaveExpFile); 
                expItemTextField.setText("");
            }
            
            
    }//GEN-LAST:event_expAddButton1ActionPerformed

    private void getStatisticButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getStatisticButtonActionPerformed
        StatisticArea.setText("");
        linesInFile.clear();
        if ((byMonth.isSelected())&&(generalChack.isSelected())){
            try {
                prepareMakeStatisticsByMonth();
                makeGeneralStatistics();
            } catch (IOException ex) {
                System.out.println("Error in module make statistic 1");
               // ex.printStackTrace();
            }         
        }else if ((byRange.isSelected())&&(generalChack.isSelected()))
            try {           
            prepareMakeStatisticsByRange(getDate(FromDate), getDate(ToDate));
            makeGeneralStatistics();
            }catch (IOException ex) {
            System.out.println("Error in module make statistic 2");
           // ex.printStackTrace();
            
        } else if ((byMonth.isSelected())&&(itemCheck.isSelected())){
            try {
                prepareMakeStatisticsByMonth();
            } catch (IOException ex) {
            System.out.println("Error in module make statistic 3");
           // ex.printStackTrace();
            }
        } else if ((byRange.isSelected())&&(itemCheck.isSelected())){
            try {
                prepareMakeStatisticsByRange(getDate(FromDate), getDate(ToDate));
                getMonthStatisticByAllItem();
            } catch (IOException ex) {
            System.out.println("Error in module make statistic 4");
           // ex.printStackTrace();
            }
        }      
  
    }//GEN-LAST:event_getStatisticButtonActionPerformed

    private void FromDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FromDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FromDateActionPerformed

    private void itemCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCheckActionPerformed
        generalChack.setSelected(true);
        if (generalChack.isSelected()&itemCheck.isSelected()){
        generalChack.setSelected(false);
    } 
        // TODO add your handling code here:
    }//GEN-LAST:event_itemCheckActionPerformed

    private void generalChackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generalChackActionPerformed
        itemCheck.setSelected(true);
        if (generalChack.isSelected()&itemCheck.isSelected()){
            itemCheck.setSelected(false);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_generalChackActionPerformed

    private void byMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byMonthActionPerformed
        byMonth.setSelected(true);
        if (byMonth.isSelected()&byRange.isSelected()){
            byRange.setSelected(false);
        }
    }//GEN-LAST:event_byMonthActionPerformed

    private void byRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byRangeActionPerformed
        byRange.setSelected(true);
        if (byMonth.isSelected()&byRange.isSelected()){
            byMonth.setSelected(false);
            ToDate.setDate(getCurDate());
            
        }
    }//GEN-LAST:event_byRangeActionPerformed

    private void cashtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cashtFieldActionPerformed

      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXDatePicker FromDate;
    private javax.swing.JTextArea StatisticArea;
    private org.jdesktop.swingx.JXDatePicker ToDate;
    private javax.swing.JCheckBox addDesctiption;
    private javax.swing.JButton addToBase;
    private javax.swing.JRadioButton byMonth;
    private javax.swing.JRadioButton byRange;
    private java.awt.Label cashLable;
    private javax.swing.JFormattedTextField cashtField;
    private java.awt.Label dateLable;
    private java.awt.Label dateLable1;
    private javax.swing.JTextArea descriptionArea;
    private javax.swing.JButton expAddButton1;
    private javax.swing.JButton expDelButton1;
    private javax.swing.JTextField expItemTextField;
    private javax.swing.JRadioButton expenseRadioButton;
    private javax.swing.JCheckBox generalChack;
    private javax.swing.JButton getStatisticButton;
    private javax.swing.JButton incAddButton;
    private javax.swing.JButton incDelButton;
    private javax.swing.JTextField incItemTextField;
    private javax.swing.JRadioButton incomeRadioButton;
    private javax.swing.JCheckBox itemCheck;
    private javax.swing.JComboBox<String> itemField;
    DefaultComboBoxModel<String> model=new DefaultComboBoxModel<>();
    private java.awt.Label itemLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList<String> jListForExpens;
    private javax.swing.JList<String> jListForIncoming;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    org.jdesktop.swingx.JXDatePicker payDate;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables


        private JFrame frame;
        private SimpleDateFormat dateFormat;
        boolean addFlag = false;
        MyJPanel panal;
        //=============CONTROLLER FIELDS===================
        DefaultListModel<String> listModel;





        private void setListModel(ListModel listModel) {
            this.listModel = (DefaultListModel) listModel;
        }

        private void createAndShowGUI() {
            // create work frame and add panel
            frame = new JFrame();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle(" \u0414\u043e\u0445\u043e\u0434\u044b & \u0420\u0430\u0441\u0445\u043e\u0434\u044b");
            //add panel to frame
            frame.getContentPane().add(this);
            frame.pack();
        }

        private void restorejListItem(File pathToFile) {
            try (final BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    listModel.addElement(line);
                }
            } catch (IOException ex) {
                System.out.println("Can't open " + pathToFile.getName() + " or file not exist in the path: " + pathToFile);
            }
        }

        //get current date for date field
        Date getCurDate() {
            Date date = new Date();
            dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            return date;
        }

        // add to jList
        void addElementToItemList(String item, File pathToFile) {
            if (listModel.contains(item)) {
                System.out.println("Already contain, change item name");
            } else {
                listModel.addElement(item);
                System.out.println("Item " + item + " add!");
                try {
                    System.out.println(pathToFile);
                    fileModel.saveInFile(item, true, pathToFile); //save item in File
                } catch (IOException ex) {
                    System.out.println("Can't save array in File");
                }
            }
        }


        //delete from jList
        void delElementFromItemList(int itemIndex, File pathToFile) {
            try {
                listModel.get(itemIndex);
                listModel.remove(itemIndex);
                
                FileWriter writer = new FileWriter(pathToFile);
                
                for (int i = 0; i < listModel.getSize(); i++) {
                writer.write(listModel.getElementAt(i));
                writer.write(System.lineSeparator());
            }
            System.out.println("IncList saved!");
            writer.close();

            } catch (IOException ex) {
                System.out.println("delIncItem Exception");
            }
        }
        




        void putElementToComboBox(File pathToFile, DefaultComboBoxModel<String> model) {
            model.removeAllElements();
            try (final BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    model.addElement(line);
                    System.out.println(line + " aadded!");
                }
            } catch (IOException ex) {
                System.out.println(pathToFile + " <---- Can't open IncFile2 or file not exist");
            }
        }

        void addToStorage(ArrayList<String> arrToAdd) {
            if (arrToAdd == null) {
                System.out.println("Error in addToStorage");
            } else {
                try {
                    setPathToSave(getDate(payDate, "MM.yyyy"));
                    System.out.println(getPathToSave());
                    fileModel.saveInFile(String.join(" ", arrToAdd), true, getPathToSave());
                } catch (IOException ex) {
                    System.out.println("Can't finf file in " + getPathToSave());
                }
            }
            System.out.println("Add to file " + getPathToSave());
        }

        //First lanch or missing <data> folder
        private void createDataDir() throws IOException {
            String ProgDir = MyJPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            File parentDir = new File(ProgDir).getParentFile();
            System.out.println("Work path: " + parentDir);
            
            setPathToSaveFolder(new File(parentDir, "data"));
            setPathToSaveIncFile(new File(pathToSaveFolder, fileModel.getIncFile()));
            setPathToSaveExpFile(new File(pathToSaveFolder, fileModel.getExpFile()));
            
            if (!pathToSaveFolder.exists()) {
                pathToSaveFolder.mkdir();
                new File(pathToSaveFolder, fileModel.getIncFile()).createNewFile();
                new File(pathToSaveFolder, fileModel.getExpFile()).createNewFile();
                System.out.println("Created <data> folder in " + parentDir);
            }
        }
        
            public static void main(String[] args) throws IOException {
            System.out.println("Wellcome to IAE program... ");
            MyJPanel panal=new MyJPanel();
            
            //panal.createDataDir();
            panal.createAndShowGUI();
        }
    }



