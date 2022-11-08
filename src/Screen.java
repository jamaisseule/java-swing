import com.asm.productmanager.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class Screen extends JFrame{
    private JTable listProducts;
    private JTextField txtID;
    private JTextField txtName;
    private JTextField txtProvider;
    private JTextField txtPrice;
    private JComboBox cbbType;
    private JSpinner spinQuantity;
    private JButton btnDelete;
    private JButton btnAdd;
    private JButton btnReset;
    private JButton btnUpdate;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JPanel panelMain;
    private JButton btnExit;
    private JButton btnSave;
    private List<Product> list = new ArrayList<>();
    private DefaultTableModel model = new DefaultTableModel();

    private final String[] columHeaders = new String[] {"ID", "Name", "Provider", "Price", "Type", "Quantity"};


    public Screen(){
        super("Product Manager");
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        setLocationRelativeTo(null);

        readFile();
        initTable();

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    StringBuilder errors = new StringBuilder(); // để hiện thông báo
                    if (txtID.getText().equals("")) {
                        errors.append("Product id must be entered. ");
                        txtID.setBackground(Color.RED);
                    }else {
                        txtID.setBackground(Color.WHITE);
                    }
                    if (txtName.getText().equals("")){
                        errors.append("Name must be entered");
                    }
                    if (!errors.isEmpty()){
                        JOptionPane.showMessageDialog(btnAdd, errors.toString());
                        return;
                    }

                    Product product = new Product();
                    product.setId(Integer.parseInt(txtID.getText()));
                    product.setName(txtName.getText());
                    product.setProvider(txtProvider.getText());
                    product.setPrice(Integer.parseInt(txtPrice.getText()));
                    product.setType(cbbType.getSelectedItem().toString());
                    product.setQuantity(Integer.parseInt(spinQuantity.getValue().toString()));

                    addProduct(product, product.getId());


                } catch (Exception e){
                    JOptionPane.showMessageDialog(btnAdd, "Error: " + e.getMessage());
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    StringBuilder errors = new StringBuilder(); // để hiện thông báo
                    if (txtID.getText().equals("")) {
                        errors.append("Product id must be entered. ");
                        txtID.setBackground(Color.RED);
                    }else {
                        txtID.setBackground(Color.WHITE);
                    }
                    if (txtName.getText().equals("")){
                        errors.append("Name must be entered");
                    }
                    if (!errors.isEmpty()){
                        JOptionPane.showMessageDialog(btnAdd, errors.toString());
                        return;
                    }

                    Product product = new Product();
                    product.setId(Integer.parseInt(txtID.getText()));
                    product.setName(txtName.getText());
                    product.setProvider(txtProvider.getText());
                    product.setPrice(Integer.parseInt(txtPrice.getText()));
                    product.setType(cbbType.getSelectedItem().toString());
                    product.setQuantity(Integer.parseInt(spinQuantity.getValue().toString()));

                    updateProduct(product);

                } catch (Exception e){
                    JOptionPane.showMessageDialog(btnAdd, "Error: " + e.getMessage());
                }

            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (txtID.getText().equals("")){
                        JOptionPane.showMessageDialog(btnDelete, "Please select the product you want to delete.");
                        return;
                    }
                    if (JOptionPane.showConfirmDialog(btnDelete, "Do you want to deleted the product ID = "
                            + txtID.getText() + "?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.NO_OPTION) {
                        return;
                    }

                    deleteProduct();
                }catch (Exception e){
                    JOptionPane.showMessageDialog(btnDelete, "Error: " + e.getMessage());
                }
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtID.setText("");
                txtName.setText("");
                txtPrice.setText("");
                txtProvider.setText("");
            }
        });


        listProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int selectedRow = listProducts.getSelectedRow();

                if (selectedRow >= 0) {
                    Product product = list.get(selectedRow);
                    setModel(product);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (txtSearch.getText().equals("")){
                        JOptionPane.showMessageDialog(btnSearch, "Please enter product you want find");
                        return;
                    }

                    Product find = findById(Integer.parseInt(txtSearch.getText()));
                    if (find != null) {
                        setModel(find);
                    } else {
                        JOptionPane.showMessageDialog(btnSearch, "The product not found");
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(btnSearch, "Error: " + e.getMessage());
                }
            }
        });


        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fExit();
            }
        });
    }

    private void refreshData(){
        model.setRowCount(0); //xoá trắng các thông tin đã nhập
        list.forEach(item -> {
            model.addRow(new Object[] {item.getId(), item.getName(), item.getProvider(),
                    item.getPrice(), item.getType(), item.getQuantity()});
        });

        model.fireTableDataChanged(); //cập nhật lại thông tin được hiển thị
    }

    public boolean addProduct(Product product, int id) {
        boolean isExisted = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id){
                isExisted = true;
                break;
            }
        }
        if (!isExisted) {
            list.add(product);
            refreshData();
            JOptionPane.showMessageDialog(btnAdd, "Add product successfully!");
            return true;
        } else {
            JOptionPane.showMessageDialog(btnAdd, "Product ID already exists. Add product does not successful!");
            return false;
        }
    }

    private void deleteProduct(){
        int selectedRow = listProducts.getSelectedRow();
        Product product = list.get(selectedRow);

        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).getId() == product.getId())){
                product = list.get(i);  //trả về phần tử hiện diện ở vị trí đã chỉ định.
                break;
            }
        }
        if (product != null) {
            list.remove(product);
            refreshData();
            JOptionPane.showMessageDialog(btnDelete, "The product has been deleted.");
        } else {
            JOptionPane.showMessageDialog(btnDelete, "The product does not found.");
        }
    }

    private void updateProduct(Product product){
        boolean isUpdated = false;
        for (int i = 0; i < list.size(); i++) {
            Product pro = list.get(i);
            if (Objects.equals(pro.getId(), product.getId())){  //kiểm tra xem hai object có bằng nhau hay không.
                list.set(i, product);
                break;
            }
        }
        if (!isUpdated) {
            refreshData();
            JOptionPane.showMessageDialog(btnUpdate, "Product successfully updated!");
        }
        else {
            JOptionPane.showMessageDialog(btnUpdate, "The product does not found.");
        }
    }

    private Product findById(int id){
        for (Product pro : list) {
            if (pro.getId() == id){
                return pro;
            }
        }
        return null;
    }

    public void setModel(Product pro) {
        txtID.setText("" + pro.getId());
        txtName.setText(pro.getName());
        txtProvider.setText(pro.getProvider());
        txtPrice.setText("" + pro.getPrice());
        cbbType.setSelectedItem(pro.getType());
        spinQuantity.setValue(pro.getQuantity());
    }

    private void readFile() {
        list.clear();  // xoá tất cả các phần khỏi mảng
        String fName = "List_Products.txt";
        try {
            File f = new File(fName);
            if (!f.exists()) {
                JOptionPane.showMessageDialog(this,fName + " doesn't exist.");
                return;
            }
            FileReader fr = new FileReader(f); //doc file
            BufferedReader br = new BufferedReader(fr); // doc van ban tu file
            String line;
            while ((line = br.readLine()) != null) { // doc tung dong
                String attribute[] = line.split(", ");
                list.add(new Product(Integer.parseInt(attribute[0]), attribute[1], attribute[2],Integer.parseInt(attribute[3]),
                        attribute[4], Integer.parseInt(attribute[5])));
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return;
        }
    }

    private void writeFile() {
        String fName = "List_Products.txt";
        try {
            File f = new File(fName);
            if (!f.exists()) {
                JOptionPane.showMessageDialog(this,fName + " doesn't exist.");
                return;
            }
            FileWriter fw = new FileWriter(f); // ghi
            PrintWriter pw = new PrintWriter(fw); // xuat van ban da ghi
            for (Product product : list) {
                pw.println(product.toString());
            }
            pw.close();
            fw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }


    private void fExit(){
        int confirmed = JOptionPane.showConfirmDialog(this, "Do you want to exit?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if(confirmed == JOptionPane.YES_OPTION){
            writeFile();
            System.exit(0);
        }
    }
//    private void readFile(){
//        FileInputStream fis = null; //tạo ra một file input stream
//        ObjectInputStream ois = null;  //tạo ra một object input stream sử dụng file input stream
//        try{
//            fis = new FileInputStream("List_Products.txt");
//            ois = new ObjectInputStream(fis);
//            list = (ArrayList<Product>)ois.readObject();
//
//        } catch (FileNotFoundException e){
//
//        } catch (Exception e){
//            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//        }finally {
//            try{
//                if(ois != null && fis != null){
//                    ois.close();
//                    fis.close(); // đóng an toàn
//                }
//            }catch (Exception e){
//                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//            }
//        }
//    }
//
//    private void writeFile(){
//        FileOutputStream fos = null;
//        ObjectOutputStream oos = null;
//        try{
//            fos = new FileOutputStream("List_Products.txt", true);
//            oos = new ObjectOutputStream(fos);
//            oos.writeObject(list);
//
//        }catch (Exception e){
//            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//        }finally {
//            try{
//                if(oos != null && fos != null) {
//                    oos.close();
//                    fos.close(); // đóng an toàn
//                }
//            }catch (Exception e){
//                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//            }
//        }
//    }



    private void initTable(){
        String[] columHeaders = new String[] {"ID", "Name", "Provider", "Price", "Type", "Quantity"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columHeaders);


        list.forEach(item -> {
            model.addRow(new Object[]{item.getId(), item.getName(), item.getProvider(),
                    item.getPrice(), item.getType(), item.getQuantity()});
        });
        listProducts.setModel(model);
    }



    public static void main(String[] args) {
        Screen s = new Screen();
        s.setContentPane(s.panelMain);
        s.setTitle("Product Manager");
        s.setVisible(true);
        s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
