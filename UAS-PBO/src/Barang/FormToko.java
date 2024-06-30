package Barang;

import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormToko extends JFrame {
    private String [] judul = {"Kode", "Nama","Harga","Stok"};
    DefaultTableModel df;
    JTable tab = new JTable();
    JScrollPane scp = new JScrollPane();
    JPanel pnl = new JPanel();
    JLabel lblkode = new  JLabel("Kode Barang");
    JTextField txkode = new JTextField(20);
    JLabel lblnama = new JLabel("Nama Barang");
    JTextField txnama = new JTextField(10);
    JLabel lblharga = new JLabel("Harga Barang");
    JTextField txharga = new JTextField(10);
    JLabel lblstok= new JLabel("Stok Barang");
    JTextField txstok = new JTextField(10);
    JButton btadd = new  JButton("Simpan");
    JButton btnew = new JButton ("Baru");
    JButton btdel = new JButton ("Hapus");
    JButton btedit = new JButton ("Ubah");
    
    FormToko(){
        super("Data Barang");
        setSize(460, 600);
        pnl.setLayout(null);
        pnl.add(lblkode);
        lblkode.setBounds(20, 10, 80, 20);
        pnl.add(txkode);
        txkode.setBounds(105, 10, 100, 20);
        pnl.add(lblnama);
        lblnama.setBounds(20, 35, 80, 20);
        pnl.add(txnama);
        txnama.setBounds(105, 35, 175, 20);
        pnl.add(lblharga);
        lblharga.setBounds(20, 60, 80, 20);
        pnl.add(txharga);
        txharga.setBounds(105, 60, 175, 20);
        pnl.add(lblstok);
        lblstok.setBounds(20, 85, 80, 20);
        pnl.add(txstok);
        txstok.setBounds(105, 85, 175, 20);
        
        pnl.add(btnew);
        btnew.setBounds(300, 10, 125, 20);
        btnew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
            btnewAksi(e);
        }
    });
        pnl.add(btadd);
        btadd.setBounds(300, 35, 125, 20);
        btadd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            btaddAksi(e);
            }
        });
    pnl.add(btedit);
    btedit.setBounds(300, 60, 125, 20);
    btedit.setEnabled(false);
    btedit.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            bteditAksi(e);
    }
  });
  pnl.add(btdel);
  btdel.setBounds(300, 85, 125, 20);
  btdel.setEnabled(false);
  btdel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
              btdelAksi(e);
      }
  });
  df = new DefaultTableModel(null, judul);
  tab.setModel(df);
  scp.getViewport().add(tab);
  tab.setEnabled(true);
  tab.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
          tabMouseClicked(evt);
      }
  });
  scp.setBounds(20, 170, 405, 130);
  pnl.add(scp);
  getContentPane().add(pnl);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setVisible(true);
}
    
void loadData(){
    try{
        Connection cn = new ConnectDB().getConnect();
        Statement st = cn.createStatement();
        String sql = "SELECT * FROM tbl_barang";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()){
            String kode, nama, harga, stok;
            kode = rs.getString("kode_barang");
            nama = rs.getString("nama_barang");
            harga = rs.getString("harga_barang");
            stok = rs.getString("stok_barang");

            String [] data = {kode, nama, harga, stok};
            df.addRow(data);
        }
        rs.close();
        cn.close();
    }  catch (SQLException ex) {
        ex.printStackTrace();
    }
}
void clearTable(){
    int numRow = df.getRowCount();
    for(int i=0; i<numRow; i++) {
        df.removeRow(0);
    }
}
void clearTextField(){
    txkode.setText(null);
    txnama.setText(null);
    txharga.setText(null);
    txstok.setText(null);
}

void simpanData(Toko M){
    try{
        Connection cn = new ConnectDB().getConnect();
        Statement st = cn.createStatement();
        String sql = "INSERT INTO tbl_barang (kode_barang, nama_barang, harga_barang, stok_barang) " +
                "VALUES ('" + M.getKode()+ "', '" + M.getNama() + "', '" + M.getHarga()+ "','"+ M.getStok() + "')";
        int result = st.executeUpdate(sql);
        cn.close();
        JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan",
                "Info Proses", JOptionPane.INFORMATION_MESSAGE);
        String [] data = {M.getKode(), M.getNama(), M.getHarga(), M.getStok()};
        df.addRow(data);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
void hapusData(String kode){
    try{
        Connection cn = new ConnectDB().getConnect();
        Statement st = cn.createStatement();
        String sql = "DELETE FROM tbl_barang WHERE kode_barang = '"+kode+"'";
        int result = st.executeUpdate(sql);
        cn.close();
        JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus", "Info Proses",
                JOptionPane.INFORMATION_MESSAGE);
        df.removeRow(tab.getSelectedRow());
        clearTextField();
    } catch (SQLException ex){
        ex.printStackTrace();
    }
}

void ubahData(Toko M, String kode) {
    try{
        Connection cn = new ConnectDB().getConnect();
        Statement st = cn.createStatement();
        String sql = "UPDATE tbl_barang SET kode_barang='"+  M.getKode()+"', nama_barang ='" + M.getNama()+"', harga_barang='" + M.getHarga()+"', stok_barang ='" + M.getStok()+"' WHERE kode_barang='" +kode+"'";
        int result = st.executeUpdate(sql);
        cn.close();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diubah","Info Proses",
                JOptionPane.INFORMATION_MESSAGE);
        clearTable();
        loadData();
    } catch (SQLException ex){
        ex.printStackTrace();
    }
}
private void btnewAksi(ActionEvent evt){
    txkode.setText(null);
    txnama.setText(null);
    txharga.setText(null);
    txstok.setText(null);
    btedit.setEnabled(false);
    btdel.setEnabled(false);
    btadd.setEnabled(true);
}
private void btaddAksi(ActionEvent evt){
    Toko M = new Toko();
    M.setKode(txkode.getText());
    M.setNama(txnama.getText());
    M.setHarga(txharga.getText());
    M.setStok(txstok.getText());
    simpanData(M);
}

private void btdelAksi(ActionEvent evt){
    int status;
    status = JOptionPane.showConfirmDialog(null,"Yakin data akan dihapus?",
            "Konfirmasi",JOptionPane.OK_CANCEL_OPTION);
    if(status == 0){
        hapusData(txkode.getText());
    }
}
private void bteditAksi(ActionEvent evt){
    Toko M = new Toko();
    M.setKode(txkode.getText());
    M.setNama(txnama.getText());
    M.setHarga(txharga.getText());
    M.setStok(txstok.getText());
    ubahData(M, tab.getValueAt(tab.getSelectedRow(),0).toString());
}
private void tabMouseClicked(MouseEvent evt){
    btedit.setEnabled(true);
    btdel.setEnabled(true);
    btadd.setEnabled(false);
    String kode, nama, harga, stok;
    kode = tab.getValueAt(tab.getSelectedRow(), 0).toString();
    nama = tab.getValueAt(tab.getSelectedRow(), 1).toString();
    harga = tab.getValueAt(tab.getSelectedRow(), 2).toString();
    stok = tab.getValueAt(tab.getSelectedRow(), 3).toString();
    txkode.setText(kode);
    txnama.setText(nama);
    txharga.setText(harga);
    txstok.setText(stok);
    
}

public static void main(String [] args) {
    new FormToko().loadData();
}

}