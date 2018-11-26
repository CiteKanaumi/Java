    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.net.URL;
     
    public class Main {
     
        public static void main(final String[] args){
            URL url = null;
            InputStreamReader isr = null;
	    String m;
	    StringBuilder buf = new StringBuilder();
	    String User;
	    
            try {
                url = new URL("https://game.capcom.com/cfn/sfv/profile/catmayor");
     
                // InputStream(�o�C�g�X�g���[��)�̂܂܂ł�HTML�͎擾�ł��邪������������
                InputStream is = url.openStream();
     
                // InputStream��UTF8��InputStreamReader(�����X�g���[��)�ɕϊ�����
                isr = new InputStreamReader(is,"UTF-8");
                
                // �ꕶ�����ɓǂݍ���
                while(true) {
                    int i = isr.read();
                    if (i == -1) {
                        break;
                    }
                    
		    buf.append(String.valueOf((char)i));
		    
		    
		    
                }
		m = buf.toString();
		System.out.println(m);
		if(m.indexOf("online") != -1){System.out.println("online");}
		else{System.out.println("offline");}
		
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }finally {
                try {
                    isr.close();
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
     
        }
    }
