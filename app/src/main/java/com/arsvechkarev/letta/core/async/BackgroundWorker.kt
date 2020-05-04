package com.arsvechkarev.letta.core.async

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class BackgroundWorker(
  private val executor: ExecutorService
) : Worker {
  
  override fun submit(block: () -> Unit): Future<*> {
    return executor.submit(block)
  }
  
  companion object {
  
    fun default() = BackgroundWorker(Executors.newFixedThreadPool(2))
  
    fun io() = BackgroundWorker(Executors.newFixedThreadPool(4))
  }
}