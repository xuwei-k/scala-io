package scalax.io

import java.io.BufferedOutputStream
import java.nio.channels.{Channels, WritableByteChannel}


/**
 * A ManagedResource for accessing and using ByteChannels.
 *
 * @see ManagedResource
 */
class WritableByteChannelResource[+A <: WritableByteChannel](opener: => A, closeAction:CloseAction[A]) extends BufferableOutputResource[A, BufferedOutputStream]
    with ResourceOps[A, WritableByteChannelResource[A]]  {
  def open() = opener
  override def acquireFor[B](f: (A) => B) = new CloseableResourceAcquirer(open,f,closeAction)()

  def prependCloseAction[B >: A](newAction: CloseAction[B]) = new WritableByteChannelResource(opener,newAction :+ closeAction)
  def appendCloseAction[B >: A](newAction: CloseAction[B]) = new WritableByteChannelResource(opener,closeAction +: newAction)

  def buffered = outputStream.buffered
  def outputStream = {
    val nResource = new ChannelOutputStreamAdapter(opener)
    val closer = ResourceAdapter.closeAction(closeAction)
    Resource.fromOutputStream(nResource)(closer)
  }
  def underlyingOutput = outputStream
  def writer(implicit sourceCodec: Codec) = {
    val nResource = new ChannelWriterAdapter(opener,sourceCodec)
    val closer = ResourceAdapter.closeAction(closeAction)
    Resource.fromWriter(nResource)(closer)
  }
  def writableByteChannel = this
}
