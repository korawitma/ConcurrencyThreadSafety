/**
 * บัญชีธนาคาร — ไฟล์ที่นิสิตต้องแก้ (ส่วนที่ 1)
 *
 * ตอนนี้คลาสนี้ ยังไม่ปลอดภัยต่อเธรด
 * balance คือ shared mutable state: หลายเธรดมองเห็นและเขียนทับกันได้
 *
 * อย่าเพิ่งแก้อะไรจนกว่าจะทำ Stage 1 ในไฟล์ README.md เสร็จ
 * ต้องเห็นมันพังด้วยตาตัวเองก่อน แล้วค่อยแก้
 */
public class Account {

    private final int id;

    /** ยอดเงินคงเหลือ — จุดที่เธรดหลายตัวแย่งกันเขียน */
    private int balance;

    /**
     * @param id            เลขบัญชี ต้องไม่ซ้ำกันในระบบเดียวกัน
     * @param initialBalance ยอดตั้งต้น ต้องไม่ติดลบ
     */
    public Account(int id, int initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("initialBalance must not be negative");
        }
        this.id = id;
        this.balance = initialBalance;
    }

    /** เลขบัญชี — ค่านี้ไม่เปลี่ยนตลอดอายุ object จึงไม่ต้องคุ้มครอง */
    public int id() {
        return id;
    }

   
    public synchronized int balance() {
        return balance;
    }

    
    public synchronized void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        balance = balance + amount;
    }

    // ---------------------------------------------------------------
    // TODO 1.2  check-then-act
    //
    // ตรงนี้อันตรายกว่า deposit เพราะมีการ "ตรวจก่อนแล้วค่อยทำ"
    // ระหว่าง if (balance >= amount) กับบรรทัดถัดไป เธรดอื่นถอนไปแล้วได้
    // ผลคือยอดติดลบ ทั้งที่โค้ดมี if ป้องกันอยู่ชัด ๆ
    //
    // @return true ถ้าถอนสำเร็จ, false ถ้าเงินไม่พอ
    // ---------------------------------------------------------------
    public synchronized boolean withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (balance >= amount) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account#" + id + "(" + balance() + ")";
    }
}
