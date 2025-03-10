package io.getquill.quat

import io.getquill.ast.{ Ast, Ident, Property }

object QuatNestingHelper {
  def valueQuat(quat: Quat): Quat =
    quat match {
      case Quat.BooleanExpression   => Quat.BooleanValue
      case p @ Quat.Product(fields) => Quat.Product(p.name, fields.toList.map { case (k, v) => (k, valueQuat(v)) })
      case other                    => other
    }

  def valuefyQuatInProperty(ast: Ast): Ast =
    ast match {
      case Property(id: Ident, name) =>
        val newQuat = valueQuat(id.quat) // Force quat value recomputation for better performance
        Property(id.copy(quat = newQuat), name)
      case Property(prop: Property, name) =>
        Property(valuefyQuatInProperty(prop), name)
      case other =>
        other
    }
}
