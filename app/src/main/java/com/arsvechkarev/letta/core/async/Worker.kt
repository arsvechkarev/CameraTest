package com.arsvechkarev.letta.core.async

import java.util.concurrent.Future

interface Worker {
  
  fun submit(block: () -> Unit): Future<*>?
}