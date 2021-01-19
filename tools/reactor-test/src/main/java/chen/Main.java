package chen;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    private static void getValInInfiniteStream() {
        Mono.just(1)
                .repeat()
                .map(e -> new Random().nextInt(10))
                .doOnNext(e -> System.out.println(e))
                .filter(e -> e.equals(0))
                .blockFirst();
    }


    private static void stopInfiniteStream() {
        Mono.just(1)
                .repeat()
                .map(e -> new Random().nextInt(10))
                .doOnNext(e -> System.out.println(e))
                .any(e -> e == 5)
                .subscribe(e -> System.out.println(e));
    }

    private static void zip(){
        Flux.create(sink -> {
            char a = 'a';
            while (a <= 'z') {
                sink.next(a++);
            }
            sink.complete();
        }).zipWith(Flux.range(1, Integer.MAX_VALUE), (a, b) -> String.format("%s.%s", b, a))
                .subscribe(e -> System.out.println(e));
    }

     private static  Sinks.Many<String> publisher = Sinks.many().multicast().onBackpressureBuffer();


    /**
     * 总结:创建Sink不消耗资源 , 创建depose , 并tryEmitNext 会长期占用线程  , 除非使用depose
     */
    private static void newPublisher() throws Exception{
//        Sinks.Many<String> publisher = Sinks.many().multicast().onBackpressureBuffer();
        //不可重用
//        Scheduler abcde = Schedulers.newSingle("abcd");
//        不可重用 , 终结手段 : Schedulers.shutdownNow(); 或者worker.depose
//        Scheduler abcde = Schedulers.newBoundedElastic(100, 1, "abcd", 5);
//        并发概率重用
//        Schedulers.shutdownNow();

//        Scheduler abcde = Schedulers.boundedElastic();
//        Scheduler abcde = Schedulers.single();
//        Scheduler abcde = Schedulers.immediate();
        ArrayList<Disposable> disposables = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            Disposable subscribe = publisher.asFlux()
                    .doOnNext(e -> System.out.println(Thread.currentThread() + " : " + e))
                    //可以在不同的Sink中复用
                    .subscribeOn(Schedulers.boundedElastic())
//                    .subscribeOn(Schedulers.immediate())
//                    .subscribeOn(abcde)
                    .subscribe();
            disposables.add(subscribe);
        }

        publisher.tryEmitNext("123");
        int i = 0;
        while (true) {

            TimeUnit.SECONDS.sleep(1);
            if (i < 5) {
                publisher.tryEmitNext("" + i);
            }
            if (i++ > 6) {
//                publisher.tryEmitComplete();
                System.out.println("complete");
                break;
            }
        }
        disposables.forEach(e -> e.dispose());
//        newPublisher();
        while (true) { TimeUnit.SECONDS.sleep(10000); }

    }

    public static void main(String[] args) throws Exception {
        newPublisher();
    }


}
