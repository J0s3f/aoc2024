package aockt.utils

import com.microsoft.z3.*
import java.math.BigDecimal

inline fun <R> Context.configureReals(mutator: ContextConfigurator.() -> R): R = ContextConfigurator(this).mutator()
inline fun <R> Context.configure(mutator: ContextConfigurator.() -> R): R = ContextConfigurator(this).mutator()
inline operator fun <R> Context.invoke(mutator: ContextConfigurator.() -> R): R = ContextConfigurator(this).mutator()

class ContextConfigurator(private val z3: Context) {

    val context: Context get() = z3

    infix fun ArithExpr<ArithSort>.gt(right: Int): BoolExpr = z3.mkGt(this, right.zr)
    infix fun ArithExpr<ArithSort>.gte(right: Int): BoolExpr = z3.mkGe(this, right.zr)
    infix fun ArithExpr<ArithSort>.lt(right: Int): BoolExpr = z3.mkLt(this, right.zr)
    infix fun ArithExpr<ArithSort>.lte(right: Int): BoolExpr = z3.mkLe(this, right.zr)

    operator fun ArithExpr<ArithSort>.times(right: Int): ArithExpr<ArithSort> = z3.mkMul(this, right.zr)
    operator fun ArithExpr<ArithSort>.div(right: Int): ArithExpr<ArithSort> = z3.mkDiv(this, right.zr)
    operator fun ArithExpr<ArithSort>.plus(right: Int): ArithExpr<ArithSort> = z3.mkAdd(this, right.zr)
    operator fun ArithExpr<ArithSort>.plus(right: Double): ArithExpr<ArithSort> = z3.mkAdd(this, right.zr)
    operator fun ArithExpr<ArithSort>.minus(right: Int): ArithExpr<ArithSort> = z3.mkSub(this, right.zr)
    operator fun ArithExpr<ArithSort>.minus(right: Double): ArithExpr<ArithSort> = z3.mkSub(this, right.zr)
    fun pow(left: ArithExpr<ArithSort>, right: Int) = z3.mkPower(left, right.zr)

    infix fun Int.gt(right: ArithExpr<ArithSort>): BoolExpr = z3.mkGt(this.zr, right)
    infix fun Int.gte(right: ArithExpr<ArithSort>): BoolExpr = z3.mkGe(this.zr, right)
    infix fun Int.lt(right: ArithExpr<ArithSort>): BoolExpr = z3.mkLt(this.zr, right)
    infix fun Int.lte(right: ArithExpr<ArithSort>): BoolExpr = z3.mkLe(this.zr, right)

    operator fun Int.times(right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkMul(this.zr, right)
    operator fun Int.div(right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkDiv(this.zr, right)
    operator fun Int.plus(right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkAdd(this.zr, right)
    operator fun Int.minus(right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkSub(this.zr, right)
    fun pow(left: Int, right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkPower(left.zr, right)

    infix fun ArithExpr<ArithSort>.eq(right: Int): BoolExpr = z3.mkEq(this, right.zr)
    infix fun ArithExpr<ArithSort>.eq(right: Double): BoolExpr = z3.mkEq(this, right.zr)
    infix fun ArithExpr<ArithSort>.neq(right: Int): BoolExpr = !z3.mkEq(this, right.zr)
    infix fun Int.eq(right: ArithExpr<ArithSort>): BoolExpr = z3.mkEq(this.zr, right)
    infix fun Int.neq(right: ArithExpr<ArithSort>): BoolExpr = !z3.mkEq(this.zr, right)

    val E: RealExpr = BigDecimal("2.7182818284590452353").zr
    val PI: RealExpr = BigDecimal("3.1415926535897932384").zr

    infix fun ArithExpr<ArithSort>.gt(right: ArithExpr<ArithSort>): BoolExpr = z3.mkGt(this, right)
    infix fun ArithExpr<ArithSort>.gte(right: ArithExpr<ArithSort>): BoolExpr = z3.mkGe(this, right)
    infix fun ArithExpr<ArithSort>.lt(right: ArithExpr<ArithSort>): BoolExpr = z3.mkLt(this, right)
    infix fun ArithExpr<ArithSort>.lte(right: ArithExpr<ArithSort>): BoolExpr = z3.mkLe(this, right)
    operator fun ArithExpr<ArithSort>.unaryMinus(): ArithExpr<ArithSort> = z3.mkUnaryMinus(this)

    infix fun BoolExpr.implies(right: BoolExpr): BoolExpr = z3.mkImplies(this, right)
    operator fun BoolExpr.not() = z3.mkNot(this)
    infix fun BoolExpr.and(right: BoolExpr): BoolExpr = z3.mkAnd(this, right)
    infix fun BoolExpr.or(right: BoolExpr): BoolExpr = z3.mkOr(this, right)
    infix fun BoolExpr.xor(right: BoolExpr): BoolExpr = z3.mkXor(this, right)

    operator fun ArithExpr<ArithSort>.times(right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkMul(this, right)
    operator fun ArithExpr<ArithSort>.div(right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkDiv(this, right)
    operator fun ArithExpr<ArithSort>.plus(right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkAdd(this, right)
    operator fun ArithExpr<ArithSort>.minus(right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkSub(this, right)
    fun pow(left: ArithExpr<ArithSort>, right: ArithExpr<ArithSort>): ArithExpr<ArithSort> = z3.mkPower(left, right)

    fun RealExpr.isInt2(): BoolExpr = z3.mkIsInteger(this)

    infix fun ArithExpr<ArithSort>.eq(right: ArithExpr<ArithSort>): BoolExpr = z3.mkEq(this, right)
    infix fun BoolExpr.eq(right: BoolExpr): BoolExpr = z3.mkEq(this, right)
    infix fun ArithExpr<ArithSort>.neq(right: ArithExpr<ArithSort>): BoolExpr = !z3.mkEq(this, right)
    infix fun BoolExpr.neq(right: BoolExpr): BoolExpr = !z3.mkEq(this, right)

    fun Real(name: String): RealExpr = z3.mkRealConst(name)
    fun Real(name: Symbol): RealExpr = z3.mkRealConst(name)
    fun Reals(spaceSeparatedNames: String): List<RealExpr> = spaceSeparatedNames.split(' ').map { Real(it) }
    fun Reals(vararg names: String): List<RealExpr> = names.map { Real(it) }
    fun Int(name: String): IntExpr = z3.mkIntConst(name)
    fun Int(name: Symbol): IntExpr = z3.mkIntConst(name)

    val Int.z get() = z3.mkInt(this)
    val Int.zr get() = z3.mkReal(this)
    val Double.zr get() = z3.mkReal(this.toString())
    val BigDecimal.zr get() = z3.mkReal(this.toString())

    val realSort: Sort = z3.realSort

    fun Solver(): Solver = z3.mkSolver()
    fun Solver(tactic: Tactic): Solver = z3.mkSolver(tactic)
    operator fun Solver.plusAssign(expr: BoolExpr): Unit = add(expr)
    operator fun Solver.plusAssign(exprs: List<BoolExpr>): Unit = exprs.forEach { add(it) }

    fun Tactic(name: String): Tactic = z3.mkTactic(name)

    fun Function(name: String, vararg paramTypes: Sort, returnType: Sort): FuncDecl<Sort> =
        z3.mkFuncDecl(name, paramTypes, returnType)

    fun <P1 : Expr<ArithSort>, R : Expr<ArithSort>> UnaryFunction(
        name: String,
        paramType: Sortish<P1>,
        returnType: Sortish<R>
    ): UnaryFunction<P1, R> =
        UnaryFunction(z3.mkFuncDecl(name, paramType.makeSortIn(z3), returnType.makeSortIn(z3)))

    fun <P1 : Expr<*>, P2 : Expr<*>, R : Expr<*>> BinaryFunction(
        name: String,
        leftParamType: Sortish<P1>,
        rightParamType: Sortish<P2>,
        returnType: Sortish<R>
    ): BinaryFunction<P1, P2, R> = BinaryFunction(
        z3.mkFuncDecl(
            name,
            arrayOf(leftParamType.makeSortIn(z3), rightParamType.makeSortIn(z3)),
            returnType.makeSortIn(z3)
        )
    )

    fun Implies(cause: BoolExpr, result: BoolExpr): BoolExpr = z3.mkImplies(cause, result)

    fun Double.asZ3Real(): RatNum = z3.mkReal(this.toString())
}

inline operator fun <reified T : Expr<*>> FuncDecl<*>.invoke(arg1: Expr<*>): T = this.apply(arg1) as T
inline operator fun <reified T : Expr<*>> FuncDecl<*>.invoke(arg1: Expr<*>, arg2: Expr<*>): T = this.apply(arg1, arg2) as T

class UnaryFunction<in P1 : Expr<*>, out R : Expr<*>>(val decl: FuncDecl<*>) {
    operator fun invoke(param: P1): R = decl.apply(param) as R
}

class BinaryFunction<in P1 : Expr<*>, in P2 : Expr<*>, out R : Expr<*>>(val decl: FuncDecl<*>) {
    operator fun invoke(leftParam: P1, rightParam: P2): R = decl.apply(leftParam, rightParam) as R
}

interface Sortish<out T : Expr<*>> {
    fun makeSortIn(z3: Context): Sort
}

object Arith : Sortish<ArithExpr<ArithSort>> {
    override fun makeSortIn(z3: Context): Sort = z3.realSort
}

object Real : Sortish<RealExpr> {
    override fun makeSortIn(z3: Context): Sort = z3.realSort
}

object Integer : Sortish<IntExpr> {
    override fun makeSortIn(z3: Context): Sort = z3.intSort
}
object Bool : Sortish<BoolExpr> {
    override fun makeSortIn(z3: Context): Sort = z3.boolSort
}
