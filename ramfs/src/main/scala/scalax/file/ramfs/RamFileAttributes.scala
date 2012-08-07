package scalax.file
package ramfs

class RamFileAttributes(path: RamPath) extends FileAttributes {
  override def all(linkOptions:LinkOption*): Set[FileAttribute[_]] =  path.node.toList.flatMap(_.attributes.map{case (key,value) => FileAttributeImpl(key,value)}).toSet
  override def apply[A](name: String, linkOptions:LinkOption*): Option[A] = path.node.flatMap(_.attributes.get(name).asInstanceOf[Option[A]]) 
  override def get[T <: BasicFileAttributes](c: Class[T], linkOptions:LinkOption*): Option[T] = None
  
  override def update(name: String, newVal: Any, linkOptions:LinkOption*): this.type = this

  override def view[T <: FileAttributeView](c: Class[T], linkOptions:LinkOption*): Option[T] = None

  override def supportsView[T <: FileAttributeView](c:Class[T]):Boolean = false
  override val supportedViewNames: Set[String] = Set.empty
}