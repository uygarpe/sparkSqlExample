import java.time.LocalDate
import java.time.temporal.ChronoUnit.MONTHS
import scala.util.Random

object SalaryGenerator {
  type EmpNo= Int

  case class Employee(empNo: EmpNo, departmentId: Int)
  case class Salary(empNo:EmpNo, salary:Int, salaryDateStr:String)

  def getSalaryDateStr(fromDate:LocalDate, acc:Long): String= {
    fromDate.plusMonths(acc).toString
  }

  def monthsBetween(fromDate:LocalDate, toDate:LocalDate): Long = {
    if(toDate.equals(LocalDate.parse("9999-01-01"))) 12l
    else MONTHS.between(fromDate,toDate)
  }


  def generateEmpNo(noOfEmployees: Int):Vector[EmpNo] = {
    Range.apply(100,100+noOfEmployees).toVector
  }

  def generateEmployees(arrEmpNo:Vector[EmpNo], noOfDepts:Int): Vector[Employee] = {
    arrEmpNo.map(x=> Employee(x,1+Random.nextInt(noOfDepts)))
  }

  def generateSalaryPerEmployee(empNo:EmpNo, noOfYears:Long, baseSalary:Int):Vector[Salary] = {
    val toDate= LocalDate.now()
    val fromDate = toDate.minusYears(noOfYears)
    (for {
      acc <- 0l to monthsBetween(fromDate,toDate)
    } yield Salary(empNo, baseSalary+Random.nextInt(100000), getSalaryDateStr(fromDate,acc))).toVector
  }


}
