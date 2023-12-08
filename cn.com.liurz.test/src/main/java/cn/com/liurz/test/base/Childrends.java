package cn.com.liurz.test.base;

public class Childrends implements Parent{
    @Override // 重写
    public void test() {
      System.out.println("Childrends test");
    }

 public  static void main(String[] args){
     Parent childs = new Childrends();
     childs.test();
 }
}
