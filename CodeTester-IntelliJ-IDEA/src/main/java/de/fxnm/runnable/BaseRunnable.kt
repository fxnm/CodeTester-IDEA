package de.fxnm.runnable

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import de.fxnm.listener.Listener
import de.fxnm.util.CodeTesterBundle
import java.util.LinkedList
import java.util.function.Consumer

abstract class BaseRunnable(private val project: Project, loggerClass: Class<*>) : Runnable {
    private val listeners: MutableList<Listener> = LinkedList()
    private var finished = false
    private var LOG: Logger = Logger.getInstance(loggerClass)

    fun project(): Project {
        return project
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    @JvmOverloads
    fun startRunnable(
        loggerMessage: String,
        toolWindowMessage: String,
        backGroundProcessName: String,
        argumentOne: Any? = null,
        argumentTwo: Any? = null,
        argumentThree: Any? = null
    ) {
        LOG.info("$loggerMessage - Arguments: ${argumentOne.toString()} | ${argumentTwo.toString()} | ${argumentThree.toString()} - Background ProcessName: $backGroundProcessName")

        backgroundLoadingBar(backGroundProcessName)

        listeners.forEach(Consumer { listener: Listener ->
            listener.scanStarting(
                toolWindowMessage, backGroundProcessName, argumentOne, argumentTwo, argumentThree
            )
        })
    }

    @JvmOverloads
    fun finishedRunnable(
        loggerMessage: String,
        toolWindowMessage: String,
        argumentOne: Any? = null,
        argumentTwo: Any? = null,
        argumentThree: Any? = null
    ) {
        if (finished) {
            LOG.info(CodeTesterBundle.message("plugin.runnable.baseRunnable.alreadyFinished"))
            return
        }

        LOG.info("$loggerMessage - Arguments: $argumentOne | ${argumentTwo.toString()} | ${argumentThree.toString()}")

        finished = true
        listeners.forEach(Consumer { listener: Listener ->
            listener.scanCompleted(
                toolWindowMessage, argumentOne, argumentTwo, argumentThree
            )
        })
    }

    @JvmOverloads
    fun failedRunnable(
        loggerMessage: String,
        toolWindowMessage: String,
        throwable: Throwable? = null,
        argumentOne: Any? = null,
        argumentTwo: Any? = null,
        argumentThree: Any? = null
    ) {
        if (finished) {
            LOG.info(CodeTesterBundle.message("plugin.runnable.baseRunnable.alreadyFinished"))
            return
        }

        LOG.info(
            "$loggerMessage - Arguments: ${argumentOne.toString()} | ${argumentTwo.toString()} | ${argumentThree.toString()}",
            throwable
        )

        finished = true
        listeners.forEach(Consumer { listener: Listener ->
            listener.scanFailed(
                toolWindowMessage, throwable, argumentOne, argumentTwo, argumentThree
            )
        })
    }

    private fun backgroundLoadingBar(processName: String) {
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return
        }
        ProgressManager.getInstance().run(object : Backgroundable(project(), processName, false) {
            override fun run(progressIndicator: ProgressIndicator) {
                LOG.info(
                    String.format(
                        CodeTesterBundle.message(
                            "plugin.runnable.baseRunnable.backgroundLoadingBar.start"
                        ), processName
                    )
                )
                while (!finished) {
                    progressIndicator.isIndeterminate = true
                }
                LOG.info(
                    String.format(
                        CodeTesterBundle.message(
                            "plugin.runnable.baseRunnable.backgroundLoadingBar.finished"
                        ), processName
                    )
                )
            }
        })
    }
}