package com.sx.ultimatebarx.operator

import com.sx.ultimatebarx.BarConfig
import com.sx.ultimatebarx.UltimateBarXManager

internal abstract class BaseOperator(val config: BarConfig = BarConfig.newInstance()) : Operator {
    protected val manager: UltimateBarXManager by lazy {
        UltimateBarXManager.getInstance()
    }

    override fun config(config: BarConfig): Operator {
        this.config.update(config)
        return this
    }

    override fun transparent(): Operator {
        config.transparent()
        return this
    }

    override fun light(light: Boolean): Operator {
        config.light(light)
        return this
    }

    override fun fitWindow(fitWindow: Boolean): Operator {
        config.fitWindow(fitWindow)
        return this
    }

    override fun drawableRes(drawableRes: Int): Operator {
        config.drawableRes(drawableRes)
        return this
    }

    override fun colorRes(colorRes: Int): Operator {
        config.colorRes(colorRes)
        return this
    }

    override fun color(color: Int): Operator {
        config.color(color)
        return this
    }
}