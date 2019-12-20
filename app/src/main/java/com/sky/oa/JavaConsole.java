package com.sky.oa;

import com.sky.oa.model.PoetryEntity;
import com.sky.oa.thread.JoinThread;
import com.sky.oa.thread.MyThread;
import com.sky.oa.thread.MyThread1;
import com.sky.sdk.utils.RegexUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class JavaConsole {

    public static void main(String[] args) {
//        BigDecimal num2 = new BigDecimal(0.88);
//        for (int i = 0; i < 0; i++) {
//            System.out.println("字符串==");
//        }

//        char uniChar = '\u039A';
//        System.out.println("字符串==" + uniChar);
//        System.out.println("字符串==" + uniChar);

//        IO();
//        leftmove();

//        IOTest();
//        readSdFile(new File("C:\\WorkSpace\\Chowder\\app\\total"));
//        readS();语数外理化生史地政

//        runableAndThread();

//        String str=new String("abc");                                     // 强引用
//        SoftReference<String> softRef=new SoftReference<String>(str);

//        stringEqualStr();
//        Executors
        PoetryEntity item = new PoetryEntity();
        try {
            item.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    private static void stringEqualStr() {
        String a = "android";
        String b = "android";
        String c = new String("android");
        String d = new String("android");
        String e = d.intern();

        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(d == c);
        System.out.println(a == e);
    }

    private static void runableAndThread() {
        //测试Runnable
        MyThread1 t1 = new MyThread1();
        new Thread(t1).start();//同一个t1，如果在Thread中就不行，会报错
        new Thread(t1).start();
        new Thread(t1).start();

        MyThread T1 = new MyThread("A");
        MyThread T2 = new MyThread("B");
        T1.start();
        T2.start();
    }

    private static void testJoin() {
        Thread thread = new JoinThread();
        thread.start();
        try {
            //主线程等待thread的业务处理完了之后再向下运行
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " -- " + i);
        }
    }

    private static void IO() throws IOException {
        char c;
        String d;

        // 使用 System.in 创建 BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter characters, 'q' to quit.");
        System.out.write('A');
        // 读取字符
        do {
//            c = (char) br.read();
            d = br.readLine();
//            System.out.println(c);
            System.out.write('A');
            System.out.println(d);
            System.out.write('A');
        } while (!d.equals("q"));
    }

    private static void leftmove() {
        int a5 = 1 << 9;
        int b5 = 61311999 & (a5);
        int e1 = 125882793 & a5;
        System.out.println("字符串==" + b5);
        System.out.println("字符串==" + e1);
        System.out.println("字符串==" + (5 & 1));
        System.out.println("字符串==" + (4 & 1));
        System.out.println("字符串==" + (true & false));
        System.out.println("字符串==" + (true & true));
    }

    @Override

    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("字符l");
    }

    private static void getClassmax() {
        System.out.println("byte==" + Byte.SIZE);
        System.out.println("byte==" + Byte.MAX_VALUE);
        System.out.println("byte==" + Byte.MIN_VALUE);
        System.out.println();

        System.out.println("Short==" + Short.SIZE);
        System.out.println("Short==" + Short.MAX_VALUE);
        System.out.println("Short==" + Short.MIN_VALUE);
        System.out.println();

        System.out.println("Integer==" + Integer.SIZE);
        System.out.println("Integer==" + Integer.MAX_VALUE);
        System.out.println("Integer==" + Integer.MIN_VALUE);
        System.out.println();

        System.out.println("Long==" + Long.SIZE);
        System.out.println("Long==" + Long.MAX_VALUE);
        System.out.println("Long==" + Long.MIN_VALUE);
        System.out.println();

        System.out.println("Float==" + Float.SIZE);
        System.out.println("Float==" + Float.MAX_VALUE);
        System.out.println("Float==" + Float.MIN_VALUE);
        System.out.println();

        System.out.println("Double==" + Double.SIZE);
        System.out.println("Double==" + Double.MAX_VALUE);
        System.out.println("Double==" + Double.MIN_VALUE);
        System.out.println();

        System.out.println("Character==" + Character.SIZE);
        System.out.println("Character==" + (int) Character.MAX_VALUE);
        System.out.println("Character==" + (int) Character.MIN_VALUE);
    }

    private static void IOTest() throws IOException {
        int count;
        String content;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        char[] chars = new char[1024];
        while ((count = reader.read(chars)) != -1) {
            content = new String(chars, 0, count);
            if (RegexUtils.isEmail(content.trim())) {
                System.out.println("成功");
            } else System.out.println("失败");
        }
        reader.reset();
//
        BufferedInputStream inputStream = new BufferedInputStream(System.in);
        byte[] butes = new byte[1024];
        while ((count = inputStream.read(butes)) != -1) {
            content = new String(butes, 0, count);
            content = content.replace('\n', ' ');
            System.out.println("第" + content + "个=");
        }
    }

    private static void readS() {
        String text = "弟子规，圣人训。首孝悌，次谨信。泛爱众，而亲仁。有余力，则学文。父母呼，应勿缓。父母命，行勿懒。父母教，须敬听。父母责，须顺承。冬则温，夏则凊。晨则省，昏则定。出必告，反必面。居有常，业无变。事虽小，勿擅为。苟擅为，子道亏。物虽小，勿私藏。苟私藏，亲心伤。亲所好，力为具。亲所恶，谨为去。身有伤，贻亲忧。德有伤，贻亲羞。亲爱我，孝何难。亲憎我，孝方贤。亲有过，谏使更。怡吾色，柔吾声。谏不入，悦复谏。号泣随，挞无怨。亲有疾，药先尝。昼夜侍，不离床。丧三年，常悲咽。居处变，酒肉绝。丧尽礼，祭尽诚。事死者，如事生。兄道友，弟道恭。兄弟睦，孝在中。财物轻，怨何生。言语忍，忿自泯。或饮食，或坐走。长者先，幼者后。长呼人，即代叫。人不在，已即到。称尊长，勿呼名。对尊长，勿见能。路遇长，疾趋揖。长无言，退恭立。骑下马，乘下车。过犹待，百步余。长者立，幼勿坐。长者坐，命乃坐。尊长前，声要低。低不闻，却非宜。进必趋，退必迟。问起对，视勿移。事诸父，如事父。事诸兄，如事兄。朝起早，夜眠迟。老易至，惜此时。晨必盥，兼漱口。便溺回，辄净手。冠必正，纽必结。袜与履，俱紧切。置冠服，有定位。勿乱顿，致污秽。衣贵洁，不贵华。上循分，下称家。对饮食，勿拣择。食适可，勿过则。年方少，勿饮酒。饮酒醉，最为丑。步从容，立端正。揖深圆，拜恭敬。勿践阈，勿跛倚。勿箕踞，勿摇髀。缓揭帘，勿有声。宽转弯，勿触棱。执虚器，如执盈。入虚室，如有人。事勿忙，忙多错。勿畏难，勿轻略。斗闹场，绝勿近。邪僻事，绝勿问。将入门，问孰存。将上堂，声必扬。人问谁，对以名。吾与我，不分明。用人物，须明求。倘不问，即为偷。借人物，及时还。人借物，有勿悭。凡出言，信为先。诈与妄，奚可焉。话说多，不如少。惟其是，勿佞巧。奸巧语，秽污词。市井气，切戒之。见未真，勿轻言。知未的，勿轻传。事非宜，勿轻诺。苟轻诺，进退错。凡道字，重且舒。勿急疾，勿模糊。彼说长，此说短。不关己，莫闲管。见人善，即思齐。纵去远，以渐跻。见人恶，即内省。有则改，无加警。唯德学，唯才艺。不如人，当自砺。若衣服，若饮食。不如人，勿生戚。闻过怒，闻誉乐。损友来，益友却。闻誉恐，闻过欣。直谅士，渐相亲。无心非，名为错。有心非，名为恶。过能改，归于无。倘掩饰，增一辜。凡是人，皆须爱。天同覆，地同载。行高者，名自高。人所重，非貌高。才大者，望自大。人所服，非言大。己有能，勿自私。人有能，勿轻訾。勿谄富，勿骄贫。勿厌故，勿喜新。人不闲，勿事搅。人不安，勿话扰。人有短，切莫揭。人有私，切莫说。道人善，即是善。人知之，愈思勉。扬人恶，即是恶。疾之甚，祸且作。善相劝，德皆建。过不规，道两亏。凡取与，贵分晓。与宜多，取宜少。将加人，先问己。己不欲，即速已。恩欲报，怨欲忘。抱怨短，报恩长。待婢仆，身贵端。虽贵端，慈而宽。势服人，心不然。理服人，方无言。同是人，类不齐。流俗众，仁者希。果仁者，人多畏。言不讳，色不媚。能亲仁，无限好。德日进，过日少。不亲仁，无限害。小人进，百事坏。不力行，但学文。长浮华，成何人。但力行，不学文。任己见，昧理真。读书法，有三到。心眼口，信皆要。方读此，勿慕彼。此未终，彼勿起。宽为限，紧用功。工夫到，滞塞通。心有疑，随札记。就人问，求确义。房室清，墙壁净。几案洁，笔砚正。墨磨偏，心不端。字不敬，心先病。列典籍，有定处。读看毕，还原处。虽有急，卷束齐。有缺坏，就补之。非圣书，屏勿视。蔽聪明，坏心志。勿自暴，勿自弃。 圣与贤，可驯致。";
        text = text.replace(" ", "");
        char[] chars = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            builder.append(chars[i]);
            if ((i + 1) % 16 == 0) builder.append("\n");
//            LogUtils.i(builder.toString());

        }
        FileWriter writer = null;
        try {
            writer = new FileWriter("C:\\WorkSpace\\Chowder\\app\\poetry1", true);
            writer.write(builder.toString());
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
            }
        }
    }

    private static String readSdFile(File file) {
        StringBuilder result = new StringBuilder();
        FileInputStream fileIn = null;
        BufferedReader bufferedIn = null;
        FileWriter writer = null;
        String line = null;
        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        boolean flag;
        try {
            fileIn = new FileInputStream(file);
            bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
            writer = new FileWriter("C:\\WorkSpace\\Chowder\\app\\total1", true);
            while ((line = bufferedIn.readLine()) != null) {
//                System.out.println(line);
                list.add(line);
            }
            long time = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
                flag = false;
                for (int j = 0; j < list1.size(); j++) {
                    if (list.get(i).equals(list1.get(j))) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    list1.add(list.get(i));
                    writer.write(list.get(i) + "\n");
                }
            }
            System.out.println("消耗时间==" + (System.currentTimeMillis() - time));
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) writer.close();
                if (bufferedIn != null) bufferedIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
            }
        }
        return result.toString();
    }

    private static String readSdFile1(File file) {
        StringBuilder result = new StringBuilder();
        FileInputStream fileIn = null;
        BufferedReader bufferedIn = null;
        FileWriter writer = null;
        String line = null;
        int position = 0;
        int filePos = 2;
        try {
            fileIn = new FileInputStream(file);
            bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
            writer = new FileWriter("C:\\WorkSpace\\Chowder\\app\\total1", true);
            while ((line = bufferedIn.readLine()) != null) {
                if (line.contains("NULL;"))
                    line = line.substring(5);
                else if (line.contains(";") && line.length() > 11)
                    line = line.substring(line.indexOf(";") + 1);
                System.out.println(line);
                line += "\n";
                if (position == 4000) {
                    writer.flush();
                    writer.close();
                    writer = new FileWriter("C:\\WorkSpace\\Chowder\\app\\total" + filePos, true);
                    filePos++;
                    position = 0;
                }
                writer.write(line);
                position++;
            }
        } catch (IOException e) {
        } finally {

            try {
                if (writer != null) writer.close();
                if (bufferedIn != null) bufferedIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
            }
        }
        return result.toString();
    }

    void reimburse() {
        //10.29；
        //周六日：；
        //总报销：
        //发票：400
    }

}
