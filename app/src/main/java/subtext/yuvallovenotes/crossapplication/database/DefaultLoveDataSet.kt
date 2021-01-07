package subtext.yuvallovenotes.crossapplication.database

import android.content.SharedPreferences
import org.koin.java.KoinJavaComponent.get
import subtext.yuvallovenotes.R
import subtext.yuvallovenotes.YuvalLoveNotesApp
import subtext.yuvallovenotes.crossapplication.models.loveitems.LoveClosure
import subtext.yuvallovenotes.crossapplication.models.loveitems.LoveOpener
import subtext.yuvallovenotes.crossapplication.models.loveitems.LovePhrase

object DefaultLoveDataSet {

    private val shardPrefs = get(SharedPreferences::class.java)
    private val userName = shardPrefs.getString(YuvalLoveNotesApp.context.getString(R.string.pref_key_user_name), "Me")
    private val loverName = shardPrefs.getString(YuvalLoveNotesApp.context.getString(R.string.pref_key_lover_name), "מתוקה שלי")

    val openers = mutableListOf(
            LoveOpener("A86D69A7-D887-407B-A542-85F26591DBC3", "יפה שלי"),
            LoveOpener("A86D69A7-D887-407B-A542-85F26591DBC3", "נסיכה שלי"),
            LoveOpener("A86D69A7-D887-407B-A542-85F26591DBC3", "מאמי"),
            LoveOpener("A86D69A7-D887-407B-A542-85F26591DBC3", "מאמוש"),
            LoveOpener("A86D69A7-D887-407B-A542-85F26591DBC3", "קושקושית שלי"),
            LoveOpener("4F8FD4A0-A8B3-44EE-AA76-DE23555CA748", "למקור היופי שלי \uD83C\uDF3A"),
    )
    val phrases = mutableListOf(
            LovePhrase("E76B818C-E822-42B1-BE24-401D22BA9485",
                    "אם פגעתי אני מצטער, הקרבה בינינו מוציאה ממני רבדים שאינם יוצאים בדרך כלל, לאהבה ישנן דרכים משונות להתבטא לפעמים. אם נפגעת, כולי תקווה שתסלחי לי, כי היחסים שלנו ממשיכים קדימה, בסוף אנחנו יחד, אנחנו חוזרים אחד אל השניה וכל הפעמים בהן עשינו זאת בעבר עומדות לצידנו. אני אוהב אותך, אני רוצה לסלוח ולהיות נסלח, אני רוצה ששוב נהיה יחד, מחוברים בלבבות \uD83C\uDF31"),
            LovePhrase("C91387BE-33EF-4923-AA02-4B8FC13C9176", "ליחסים יש את המורכבות שלהם, בייחוד בכל הנוגע לאמירת האמת לבן הזוג. את אומרת לי את האמת, זו היפה וזו הכואבת, וגם כשהיא כואבת, בטווח הארוך היא תהיה בשירות של האהבה. כל עוד הכוונה שלך היא לבנות את שנינו, מילותייך יהיו בשירות האהבה. תמשיכי לומר לי את האמת אהובתי, אל תפחדי, אם אפגע אקום שוב, כמו שקמתי מאז ומתמיד. חוסר המודעות כואב הרבה יותר מאור הפלורסנט של מילים חושפניות, גם אם הן מציפות באותו הרגע. החיים לצידך הם הזדמנות לכוונן ולתאם את עצמי עד שהנוכחות שלי תהיה עוצמתית, גמישה וקשובה לרגע במלואו, לשם כך אני תמיד מחפש את האמת המזדקקת מתוך המפגש ביני ובינך."),
            LovePhrase("4858FAA7-73E6-4B4E-98BD-3A507A4957BC", "לאחר שנים שנהיה ביחד תישאר האהבה, זו הנבואה שלי, זו הידיעה שבגללה בחרתי בך ונבחרתי על ידך, כי ידענו. הרגשנו בשיחות הטלפון הארוכות וההתקרבות של משבר הקורונה, הבנו שכאן ננצא החיבור שיימשך, חזק יותר מהכוחות של כל הקשיים והמחלוקות והריבים. אנחנו קשורים זה בזה ומתקדמים יחד, לפעמים אני מושך קדימה, לפעמים את,  אך אין ספק שזה קדימה. כמה קל להבחין זאת, החיים שלי כל כך התקדמו כשאני יחד איתך ואני יודע שגם את מרגישה כך מהצד שלך. בואי נמשיך את זה מאמי, בואי ניתן לאהבה להעמיק עוד ועוד עם השנים, זו הנבואה שלי והיא מגשימה את עצמה \u2066\uD83C\uDF3A"),
//            LovePhrase("", ""),
    )
    val closures = mutableListOf(
            LoveClosure("4B9BEB6B-5E16-46EF-BBF1-14ACBC9AC0A9", "הלב שלי שלך\u2066❤️\u2069"),
            LoveClosure("5EA7E6BE-5235-4FFA-B31F-1E3F58BF38F3", "אני"),
    )

}
