package course7

object RouterExample extends App {
  import cats.implicits._

  case class TaxForm(id: String, formType: String, giinNumber: Option[String])

  val maker =  new RouterMaker[TaxForm]{}
  import maker._

  case class InputCheck(form: TaxForm, message: String)
  case class AuditLog(form: TaxForm, alert: String)

  val nullRouter = route(_.id == null) to[InputCheck, Any](form => InputCheck(form, "Tax from ID cannot be null！"))
  val auditRouter = route(taxForm => taxForm.formType == "W9" && taxForm.giinNumber.isEmpty) to (AuditLog(_, "W9 missing GIIN"))

  val taxFormRouter = nullRouter <+> auditRouter

  println(taxFormRouter.process(TaxForm(null, "W9", Some("SomeGII"))))
  println(taxFormRouter.process(TaxForm("ss", "W9", None)))
  println(taxFormRouter.process(TaxForm("ss", "W9", Some("SomeGII"))))
}
