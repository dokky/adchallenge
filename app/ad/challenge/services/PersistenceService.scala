//package ad.challenge.services
//
//import java.io.{File, IOException}
//import java.util.concurrent.TimeUnit._
//import java.util.concurrent.{CountDownLatch, ExecutorService, Executors}
//
//import org.apache.cassandra.config.DatabaseDescriptor;
//import org.apache.cassandra.db.commitlog.CommitLog;
//import org.apache.cassandra.exceptions.ConfigurationException;
//import org.apache.cassandra.io.util.FileUtils;
//import org.apache.cassandra.service.CassandraDaemon;
//
//import org.apache.commons.logging.Log
//
///**
//  * Created by stanislavd on 14/05/2016.
//  */
//class PersistenceService {
//  private var cassandraDaemon: Nothing = null
//  private val launchedYamlFile: String = null
//  private val log: Log = null
//
//  @throws[TTransportException]
//  @throws[IOException]
//  @throws[ConfigurationException]
//  def startEmbeddedCassandra(file: File, tmpDir: String, timeout: Long) {
//    if (cassandraDaemon != null) {
//      return
//    }
//    checkConfigNameForRestart(file.getAbsolutePath)
//    log.debug("Starting cassandra...")
//    log.debug("Initialization needed")
//    System.setProperty("cassandra.config", "file:" + file.getAbsolutePath)
//    System.setProperty("cassandra-foreground", "true")
//    System.setProperty("cassandra.native.epoll.enabled", "false")
//    if (System.getProperty("log4j.configuration") == null) {
//      copy(DEFAULT_LOG4J_CONFIG_FILE, tmpDir)
//      System.setProperty("log4j.configuration", "file:" + tmpDir + DEFAULT_LOG4J_CONFIG_FILE)
//    }
//    cleanupAndLeaveDirs
//    val startupLatch: CountDownLatch = new CountDownLatch(1)
//    val executor: ExecutorService = Executors.newSingleThreadExecutor
//    executor.execute(new Runnable() {
//      def run {
//        cassandraDaemon = new Nothing
//        cassandraDaemon.activate
//        startupLatch.countDown
//      }
//    })
//    try {
//      if (!startupLatch.await(timeout, MILLISECONDS)) {
//        log.error("Cassandra daemon did not start after " + timeout + " ms. Consider increasing the timeout")
//        throw new AssertionError("Cassandra daemon did not start within timeout")
//      }
//    }
//    catch {
//      case e: InterruptedException => {
//        log.error("Interrupted waiting for Cassandra daemon to start:", e)
//        throw new AssertionError(e)
//      }
//    } finally {
//      executor.shutdown
//    }
//  }
//
//}
