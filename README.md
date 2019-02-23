# Stream
Android上简化的流式编程框架

源于开发的时候想用流式编程，但是Agera和Rx概念多、逻辑复杂。

这个库没有那么多概念。而且对数据流的处理操作符比较简单自由。在最终订阅者接收之前，可以“一流到底”。

暂时还只是个练手和局部使用的小实验。实验一段时间才在真正项目用。

使用示例
```java
        Stream
            .from("String 1")
            .to(new Action<String, Integer>() {
                @Override
                public Integer action(String input) {
                    assertEquals(input, "String 1");

                    return 100;
                }
            })
            .on(SchedulerFactory.ui())
            .to(new AsyncAction<Integer, Double>() {
                @Override
                public void action(Integer input, Result<Double> result) {
                    assertEquals(input, Integer.valueOf(100));
                    result.result(1.01);
                }
            })
            .to(new Action<Double, String>() {
                @Override
                public String action(Double input) {
                    assertEquals(input, Double.valueOf(1.01));
                    return "String 2";
                }
            })
            .on(SchedulerFactory.io())
            .subscribe(new Subsriber<String>() {
                @Override
                public void subscribe(String value) {
                    assertEquals(value, "String 2");
                }
            });
```
