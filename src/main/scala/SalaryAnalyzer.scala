import SalaryGenerator._



object SalaryAnalyzer {
  def main(args: Array[String]): Unit = {
    val empNos = generateEmpNo(noOfEmployees = 10)
    val employees= generateEmployees(empNos,noOfDepts = 4)
    val baseSalary = 50000
    val salaries = employees.flatMap(x=> generateSalaryPerEmployee(x.empNo, noOfYears = 2, baseSalary))
    val spark = org.apache.spark.sql.SparkSession.builder
      .master("local[*]")
      .appName("Spark SQL example")
      .getOrCreate

    import spark.implicits._

    val employeesDf = employees.toDF
    val salariesDf = salaries.toDF

    salariesDf.createOrReplaceTempView("salary")
    employeesDf.createOrReplaceTempView("employee")

    val res= spark.sql("select s.empNo, s.salary,s.salaryDateStr, e.departmentId , dense_rank() over (partition by e.departmentId order by s.salary desc) as denserank from employee e, salary s where e.empNo=s.empNo")
    res.filter("denserank<4").orderBy("departmentId").show(30)


  }
}
