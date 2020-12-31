package chen;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

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



}
