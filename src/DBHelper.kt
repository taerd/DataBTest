import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DBHelper(
    val dbName : String,
    val address : String = "localhost",
    val port : Int = 3306,
    val user : String = "root",
    val password : String = "root"
) {
    private var connection : Connection?=null
    init{
        try{
            connection =
                DriverManager.getConnection(//статический метод getConnection возвращает объект типа connection
                "jdbc:mysql://$address:$port/$dbName?serverTimezone=UTC",//ссылка для доступа( протокол доступа jdbc..)
                    user,
                    password
                )
            /**
             * Демонстрационная часть
             */
            val s = connection?.createStatement() //создание утверждения
            val sql_create ="create table if not exists `test` (\n" +
                    "    `id` int primary key auto_increment,\n" +
                    "    `text_field` varchar(50) not null,\n" +
                    "    `int_field` int default 0\n" +
                    ")"
            s?.execute(sql_create)
            s?.execute ("delete from `test`")

/*
            (1..10).forEach{
                val sql_insert = "insert into `test` (text_field,int_field) values ('ТекСтовОе ПоЛе :)',$it)"
                s?.execute(sql_insert)//Добавление записей в таблицу
            }
*/
            //Защита от sql инъекций
            (1..10).forEach{
                val ps = connection?.prepareStatement(
                    "insert into `test` (text_field,int_field) values (?,?)"
                )
                ps?.setString(1,"Здесь было текстовое поле")
                ps?.setInt(2,it)
                val rows = ps?.executeUpdate()//количество строк подходящих под запрос
                println(rows)
            }
        }catch (e: SQLException){
            println("Ошибка создания таблицы: \n${e.toString()}")
        }
    }
}