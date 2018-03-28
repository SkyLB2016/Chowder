package com.sky.chowder.ui.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.OnClick
import com.sky.SkyApp
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.ui.adapter.MainAdapter
import com.sky.chowder.ui.presenter.MainP
import com.sky.utils.AppUtils
import com.sky.utils.JumpAct
import com.sky.utils.LogUtils
import kotlinx.android.synthetic.main.content_main.*
import java.io.File


/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainActivity : BasePActivity<MainP>(), Toolbar.OnMenuItemClickListener, IMainView {
//    override fun onCreate(savedInstanceState: Bundle?) {
//super.onCreate(savedInstanceState)
//Debug.startMethodTracing()
//    }
//    override fun onDestroy() {
//super.onDestroy()
//Debug.stopMethodTracing()
//    }

    private lateinit var adapter: MainAdapter
    public override fun getLayoutResId(): Int = R.layout.activity_main
    override fun creatPresenter() = MainP(this)
    public override fun initialize() {
        baseTitle.setLeftButton(-1)
        recycler?.setHasFixedSize(true)
        adapter = MainAdapter(R.layout.adapter_main)
        recycler.adapter = adapter

        adapter?.setOnItemClickListener { _, position -> JumpAct.jumpActivity(this, adapter.datas[position].componentName) }
        adapter?.setOnItemLongClickListener { _, _ ->
            showToast("长按监听已处理")
            true
        }
        AppUtils.isPermissions(this,
                arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE),
                intArrayOf(0, 0, 0)
        )
    }

    override fun setData(data: List<ActivityModel>) {
        adapter?.datas = data
    }

    @OnClick(R.id.fab1)
    fun fab1OnClick() {
        var text = getString(R.string.cezi).trim().replace(" ", "")
        LogUtils.i("总长==${text.length}")
//        孟子36680
    }
    @OnClick(R.id.fab)
    fun fabOnClick() {
//《孟子》“四书 ”之一。战国中期孟子及其弟子万章、公孙丑等著。为孟子、孟子弟子、再传弟子的记录。最早见于赵岐《孟子题辞》：“此书，孟子之所作也，故总谓之《孟子》”。《汉书·艺文志》著录《孟子》十一篇，现存七篇十四卷。总字数三万五千余字，286章。相传另有《孟子外书》四篇，已佚(今本《孟子外书》系明姚士粦伪作)。书中记载有孟子及其弟子的政治、教育、哲学、伦理等思想观点和政治活动。 　　主要作者简介： 　　孟子(约公元前372年-公元前289年)，名轲，字子舆，战国中期鲁国邹人(今山东邹县东南部人)，距离孔子的故乡曲阜不远。是著名的思想家、政治家、教育家，孔子学说的继承者，儒家的重要代表人物。相传孟子是鲁国贵族孟孙氏的后裔，幼年丧父，家庭贫困，曾受业于子思(孔伋，是孔子的孙子)。学成以后，以士的身份游说诸侯，企图推行自己的政治主张，到过梁(魏)国、齐国、宋国、滕国、鲁国。当时几个大国都致力于富国强兵，争取通过武力的手段实现统一。而他继承了孔子“仁”的思想并将其发展成为“仁政”思想，被称为“亚圣”。 　　孟子的出生距孔子之死(前479)大约百年左右。关于他的身世，流传下来的已很少，《韩诗外传》载有他母亲“断织”的故事，《列女传》载有他母亲“三迁”和“去齐”等故事，可见他得力于母亲的教育不少。据《列女传》和赵岐《孟子题辞》说，孟子曾受教于孔子的孙子子思。但从年代推算，似乎不可信。《史记.孟子荀卿列传》说他“受业子思之门人”，这倒是有可能的。无论是受业于子思也罢，子思门人也罢，孟子的学说都受到孔子思想的影响。所以，荀子把子思和孟子列为一派，这就是后世所称儒家中的思孟学派。和孔子一样，孟子也曾带领学生游历魏、齐、宋、鲁、滕、薛等国，并一度担任过齐宣王的客卿。由于他的政治主张也与孔子的一样不被重用，所以便回到家乡聚徒讲学，与学生万章等人著书立说，“序《诗》《书》，述仲尼之意，作《孟子》七篇。”(《史记.孟子荀卿列传》)今天我们所见的《孟子》七篇，每篇分为上下，约三万五千字，一共二百六十章。但《汉书.艺文志》著录“孟子十一篇”，比现存的《孟子》多出四篇。赵岐在为《孟子》作注时，对十一篇进行了鉴别，认为七篇为真，七篇以外的四篇为伪篇。东汉以后，这几篇便相继失佚了。 　　赵岐在《孟子题辞》中把《孟子》与《论语》相比，认为《孟子》是“拟圣而作”。所以，尽管《汉书.文艺志》仅仅把《孟子》放在诸子略中，视为子书，但实际上在汉代人的心目中已经把它看作辅助“经书”的“传”书了。汉文帝把《论语》、《孝经》、《孟子》、《尔雅》各置博士，便叫“传记博士”。到五代后蜀时，后蜀主孟昶命令人楷书十一经刻石，其中包括了《孟子》，这可能是《孟子》列入“经书”的开始。后来宋太宗又翻刻了这十一经。到南宋孝宗时，朱熹编《四书》列入了《孟子》，正式把《孟子》提到了非常高的地位。元、明以后又成为科举考试的内容，更是读书人的必读之书了。 　　主要封赠： 　　战国齐宣王在稷下学宫册封的第一任“上大夫”就是孟子。 　　1083年(宋元丰6年)，升邹国公。 　　1330年(元至顺1年)，加赠为邹国亚圣公。 　　1530年(明嘉靖9年)，奉为亚圣，罢公爵。 　　明景泰二年，孟子嫡派后裔被封为翰林院五经博士，子孙世袭，一直到民国3年，73代翰林院五经博士孟庆棠改封奉祀官，民国24年改称亚圣奉祀官。 　　著作介绍： 　　《孟子》一书七篇，是战国时期孟子的言论汇编，记录了孟子与其他诸家思想的争辩，对弟子的言传身教，游说诸侯等内容，由孟子及其弟子(万章等)共同编撰而成。《孟子》记录了孟子的治国思想、政治观点(仁政、王霸之辨、民本、格君心之非，民为贵社稷次之君为轻)和政治行动，成书大约在战国中期，属儒家经典著作。其学说出发点为性善论，主张德治。南宋时朱熹将《孟子》与《论语》《大学》《中庸》合在一起称“四书”。自从宋、元、明、清以来，都把它当做家传户诵的书。就像今天的教科书一样。《孟子》是四书中篇幅最大的部头最重的一本，有三万五千多字，从此直到清末，“四书”一直是科举必考内容。《孟子》这部书的理论，不但纯粹宏博，文章也极雄健优美。(五经：《诗》《书》《礼》《易》《春秋》) 　　《孟子》是记录孟轲言行的一部著作，也是儒家重要经典之一。篇目有：(一)《梁惠王》上、下，(二)《公孙丑》上、下，(三)《滕文公》上、下，(四)《离娄》上、下，(五)《万章》上、下，(六)《告子》上、下，(七)《尽心》上、下。 梁惠王章句上 梁惠王章句下 公孙丑章句上 公孙丑章句下 滕文公章句上 滕文公章句下 离娄章句上 离娄章句下 万章章句上 万章章句下 告子章句上 告子章句下 尽心章句上 尽心章句下 　　《孟子》行文气势磅礴，感情充沛，雄辩滔滔，极富感染力，流传后世，影响深远，成为儒家经典著作之一。 　　《史记·孟荀列传》：“孟轲所如不合，退与万章之徒序《诗》、《书》，述仲尼之意，作《孟子》七篇。”谓《孟子》七篇由孟轲自作，赵岐《孟子题辞》曰：“此书孟子之所作也，故总谓之《孟子》。”又曰：“于是退而论集，所与高弟弟子公孙丑、万章之徒，难疑答问，又自撰其法度之言，着书七篇。”此亦主孟子自撰。清阎若璩《孟子生卒年月考》亦以孟子自作是，且曰：“《论语》成于门人之手，故记圣人容貌甚悉。七篇成于己手，故但记言语或出处耳。”但考诸《孟子》，孟轲所见时君如梁惠王、梁襄王、齐宣王、邹穆公、滕文公、鲁平公等皆称谥号，恐非孟子自作时所为也;又记孟子弟子乐正子、公都子、屋卢子皆以“子”称，也断非孟子之所为，其编定者极可能是孟子的弟子。成书大约在战国中期。 　　《孟子》的主要注本有《孟子注疏》，《四部备要》本14卷;《孟子集注》，《四部备要》本7卷;《孟子正义》，《四部备要》本30卷。另有今人杨伯峻《孟子译注》(中华书局本)。 孟子 　　孟子序说（朱熹四书集注） 　　史记列传曰：“孟轲，驺人也，受业子思之门人。道既通，游事齐宣王，宣王不能用。适梁，梁惠王不果所言，则见以为迂远而阔于事情。当是之时，秦用商鞅，楚魏用吴起，齐用孙子、田忌。天下方务于合从连衡，以攻伐为贤。而孟轲乃述唐、虞、三代之德，是以所如者不合。退而与万章之徒序诗书，述仲尼之意，作孟子七篇。” 　　韩子曰：“尧以是传之舜，舜以是传之禹，禹以是传之汤，汤以是传之文、武、周公，文、武、周公传之孔子，孔子传之孟轲，轲之死不得其传焉。荀与扬也，择焉而不精，语焉而不详。” 　　又曰：“孟氏醇乎醇者也。荀与扬，大醇而小疵。” 　　又曰：“孔子之道大而能博，门弟子不能遍观而尽识也，故学焉而皆得其性之所近。其后离散，分处诸侯之国，又各以其所能授弟子，源远而末益分。惟孟轲师子思，而子思之学出于曾子。自孔子没，独孟轲氏之传得其宗。故求观圣人之道者，必自孟子始。” 　　又曰：“扬子云曰：‘古者杨墨塞路，孟子辞而辟之，廓如也。’夫杨墨行，正道废。孟子虽贤圣，不得位。空言无施，虽切何补。然赖其言，而今之学者尚知宗孔氏，崇仁义，贵王贱霸而已。其大经大法，皆亡灭而不救，坏烂而不收。所谓存十一于千百，安在其能廓如也?然向无孟氏，则皆服左衽而言侏离矣。故愈尝推尊孟氏，以为功不在禹下者，为此也。”或问于程子曰：“孟子还可谓圣人否?”程子曰：“未敢便道他是圣人，然学已到至(或圣)处。” 　　程子又曰：“孟子有功于圣门，不可胜言。仲尼只说一个仁字，孟子开口便说仁义。仲尼只说一个志，孟子便说许多养气出来。只此二字，其功甚多。” 　　又曰：“孟子有大功于世，以其言性善也。” 　　又曰：“孟子性善、养气之论，皆前圣所未发。” 　　又曰：“学者全要识时。若不识时，不足以言学。颜子陋巷自乐，以有孔子在焉。若孟子之时，世既无人，安可不以道自任。” 　　又曰：“孟子有些英气。纔有英气，便有圭角，英气甚害事。如颜子便浑厚不同，颜子去圣人只豪发闲。孟子大贤，亚圣之次也。”或曰：“英气见于甚处?”曰：“但以孔子之言比之，便可见。且如冰与水精非不光。比之玉，自是有温润含蓄气象，无许多光耀也。” 　　杨氏曰：“孟子一书，只是要正人心，教人存心养性，收其放心。至论仁、义、礼、智，则以恻隐、善恶、辞让、是非之心为之端。论邪说之害，则曰：‘生于其心，害于其政。’论事君，则曰：‘格君心之非’，‘一正君而国定’。千变万化，只说从心上来。人能正心，则事无足为者矣。大学之修身、齐家、治国、平天下，其本只是正心、诚意而已。心得其正，然后知性之善。故孟子遇人便道性善。欧阳永叔却言‘圣人之教人，性非所先’，可谓误矣。人性上不可添一物，尧舜所以为万世法，亦是率性而已。所谓率性，循天理是也。外边用计用数，假饶立得功业，只是人欲之私。与圣贤作处，天地悬隔。” 　　“孟子序说”乃是朱熹编撰《四书》时所加，《孟子》原文中是没有的，见朱熹《四书章句集注·孟子集注》卷首。



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {//跳转到指定的别的APP的activity
            R.id.action_01 -> startOther("com.cx.architecture", "com.cx.architecture.ui.activity.LoginActivity")
            R.id.action_02 -> startOther("com.gc.materialdesigndemo", "com.gc.materialdesigndemo.ui.MainActivity")
        }
        return false
    }

    private fun startOther(packageName: String, componentName: String) {
        try {
            JumpAct.jumpActivity(this@MainActivity, packageName, componentName)
        } catch (e: ActivityNotFoundException) {
            showToast("程序未安装")
        }
    }

    private var lastBack: Long = 0
    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        if (now - lastBack > 3000) {
            showToast(getString(R.string.toast_exit))
            lastBack = now
        } else super.onBackPressed()
    }

    /**
     * 文件属性
     */
    private fun fileTest() {
        val f = File(SkyApp.getInstance().fileCacheDir + "pass.txt")
        System.out.println(f.getParent())//返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null
        System.out.println(f.getName())//返回由此抽象路径名表示的文件或目录的名称
        System.out.println(f.exists())//测试此抽象路径名表示的文件或目录是否存在
        System.out.println(f.getAbsoluteFile())// 返回此抽象路径名的绝对路径名形式
        System.out.println(f.getAbsolutePath())//返回此抽象路径名的规范路径名字符串
        System.out.println(f.getPath())//将此抽象路径名转换为一个路径名字符串
        System.out.println(f.hashCode())//计算此抽象路径名的哈希码
        System.out.println(f.length())//返回由此抽象路径名表示的文件的长度
        System.out.println(f.list())// 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录
        System.out.println(f.mkdir())//创建此抽象路径名指定的目录
    }
}