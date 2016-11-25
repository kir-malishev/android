package ru.samsung.itschool.magicball;

public class Item {
    /**
     * Заголовок
     */
    String header;
 
    /**
     * Подзаголовок
     */
    String subHeader;
    
    
    String type;
 
    /**
     * Конструктор создает новый элемент в соответствии с передаваемыми
     * параметрами:
     * @param h - заголовок элемента
     * @param s - подзаголовок
     * @param t - тип вопроса
     */
    Item(String h, String s, String t){
    	this.type=t;
        this.header=h;
        this.subHeader=s;
    }
 
    //Всякие гетеры и сеттеры
    public String getType()
    {
    	return type;
    }
    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }
    public String getSubHeader() {
        return subHeader;
    }
    public void setSubHeader(String subHeader) {
        this.subHeader = subHeader;
    }
 
}