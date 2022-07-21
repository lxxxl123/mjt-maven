package chen.javaBasic.reference;


import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Queue;

// 重点看下这个类的实现，关注两个 releaseConnection() 方法
public class ConnectionPool {

    private Queue<Connection> _pool = new LinkedList<Connection>();

	// 创建一个引用队列
    private ReferenceQueue<Object> _refQueue = new ReferenceQueue<Object>();

    private IdentityHashMap<Object,Connection> _ref2Cxt = new IdentityHashMap<Object,Connection>();
    private IdentityHashMap<Connection,Object> _cxt2Ref = new IdentityHashMap<Connection,Object>();

    public Connection getConnection() throws SQLException {
        while (true) {
            // 拿不到池中连接的话就一直自旋
            synchronized (this) {
                if (_pool.size() > 0) {
                    return wrapConnection(_pool.remove());
                }
            }
            // 顾名思义，等待GC清理资源后执行相应操作
            tryWaitingForGarbageCollector();
        }
    }

    private void tryWaitingForGarbageCollector() {
        try {
            Reference<?> ref = _refQueue.remove(100);
            // 如果引用队列中能够拿到引用，证明连接对象被GC回收，此时应该对连接池执行相应的清理逻辑
            if (ref != null) {
                releaseConnection(ref);
            }
        } catch (InterruptedException ignored) {
            // we have to catch this exception, but it provides no information here
            // a production-quality pool might use it as part of an orderly shutdown
        }
    }
	
    private synchronized Connection wrapConnection(Connection cxt) {
//        Connection wrapped = new PooledConnection();
        // 由于虚引用的get()方法返回永远是null，所以虚引用通常需要搭配引用队列一起使用，这里就将虚引用与一个引用队列绑定在一起
//        PhantomReference<Connection> ref = new PhantomReference<Connection>(wrapped, _refQueue);
//        _cxt2Ref.put(cxt, ref);
//        _ref2Cxt.put(ref, cxt);
//        System.err.println("Acquired connection " + cxt );
//        return wrapped;
        return null;
    }

    // 这个是使用者主动释放连接时调用的方法
    synchronized void releaseConnection(Connection cxt) {
        Object ref = _cxt2Ref.remove(cxt);
        _ref2Cxt.remove(ref);
        _pool.offer(cxt);
        System.err.println("Released connection " + cxt);
    }

    // 这个是当使用者忘记主动释放连接时连接池本身被动的释放连接的方法，应该结合getConnection()和tryWaitingForGarbageCollector()一起看
    private synchronized void releaseConnection(Reference<?> ref) {
        Connection cxt = _ref2Cxt.remove(ref);
        if (cxt != null) {
            releaseConnection(cxt);
        }
    }

}

