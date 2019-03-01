package org.forpdi.system;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.properties.SystemConfigs;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.planning.attribute.AggregateIndicator;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.attribute.AttributeHelper;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.attribute.types.ActionPlanField;
import org.forpdi.planning.attribute.types.BudgetField;
import org.forpdi.planning.attribute.types.Currency;
import org.forpdi.planning.attribute.types.NumberField;
import org.forpdi.planning.attribute.types.Percentage;
import org.forpdi.planning.attribute.types.ResponsibleField;
import org.forpdi.planning.attribute.types.ScheduleField;
import org.forpdi.planning.attribute.types.SelectPlan;
import org.forpdi.planning.attribute.types.TableField;
import org.forpdi.planning.attribute.types.TextArea;
import org.forpdi.planning.attribute.types.enums.FormatValue;
import org.forpdi.planning.document.DocumentAttribute;
import org.forpdi.planning.document.DocumentBS;
import org.forpdi.planning.document.DocumentSection;
import org.forpdi.planning.fields.FieldsBS;
import org.forpdi.planning.fields.actionplan.ActionPlan;
import org.forpdi.planning.fields.budget.BudgetDTO;
import org.forpdi.planning.fields.schedule.ScheduleInstance;
import org.forpdi.planning.fields.table.TableFields;
import org.forpdi.planning.fields.table.TableInstance;
import org.forpdi.planning.fields.table.TableStructure;
import org.forpdi.planning.fields.table.TableValues;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanBS;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureHelper;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forrisco.core.item.FieldItem;
import org.forrisco.core.item.FieldSubItem;
import org.forrisco.core.item.Item;
import org.forrisco.core.item.ItemBS;
import org.forrisco.core.item.SubItem;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.plan.PlanRiskBS;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.policy.PolicyBS;
import org.forrisco.core.unit.Unit;
import org.forrisco.core.unit.UnitBS;
import org.forrisco.risk.Incident;
import org.forrisco.risk.Monitor;
import org.forrisco.risk.Risk;
import org.forrisco.risk.RiskBS;
import org.forrisco.risk.RiskLevel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;
import org.jboss.logging.Logger;


public class PDFgenerate {
	
	@Inject
	@Current
	private CompanyDomain domain;
	@Inject
	private UserBS userBS;
	@Inject
	private PlanBS planBS;
	@Inject
	private DocumentBS docBS;

	@Inject
	private StructureBS structureBS;
	@Inject
	StructureHelper structHelper;
	@Inject
	private AttributeHelper attrHelper;
	
	@Inject
	private PolicyBS policyBS;
	@Inject
	private PlanRiskBS planriskBS;
	@Inject
	private UnitBS unitBS;
	@Inject
	private ItemBS itemBS;
	@Inject
	private FieldsBS fieldsBS;
	@Inject
	private RiskBS riskBS;
	

	protected final Logger LOGGER = Logger.getLogger(this.getClass());


	
/**
 * Exporta documento PDF
 * 
 * @param author
 *            Autor do documento.
 * @param title
 *            Título do documento.
 * @param lista
 *            Lista de seções a serem exportadas.
 * @return InputStream Fluxo de entrada do arquivo PDF.
 * @throws IOException
 * @throws DocumentException
 * @throws SQLException
 * @throws ClassNotFoundException
 */
public InputStream exportDocument(String author, String title, String lista)
		throws IOException, DocumentException, SQLException, ClassNotFoundException {

	class TOCEvent extends PdfPageEventHelper {

		protected int counter = 0;
		protected List<SimpleEntry<String, SimpleEntry<String, Integer>>> toc = new ArrayList<>();

		String lastText = "";

		@Override
		public void onGenericTag(PdfWriter writer, com.itextpdf.text.Document document, Rectangle rect,
				String text) {
			if (text != lastText) {
				String name = "dest" + (counter++);
				int page = writer.getPageNumber();
				// LOGGER.info(text);
				toc.add(new SimpleEntry<String, SimpleEntry<String, Integer>>(text,
						new SimpleEntry<String, Integer>(name, page)));
			}
			lastText = text;
			// writer.addNamedDestination(name, page, new
			// PdfDestination(PdfDestination.FITH, rect.getTop()));

		}

		public List<SimpleEntry<String, SimpleEntry<String, Integer>>> getTOC() {
			return toc;
		}

	}

	com.itextpdf.text.Document document = new com.itextpdf.text.Document();
	com.itextpdf.text.Document coverDocument = new com.itextpdf.text.Document();
	com.itextpdf.text.Document preTextDocument = new com.itextpdf.text.Document();
	com.itextpdf.text.Document summaryDocument = new com.itextpdf.text.Document();

	File outputDir;
	final String storagePath = SystemConfigs.getConfig("store.pdfs");
	if (storagePath == null || storagePath.equals("") || storagePath.equals("${store.pdfs}")) {
		outputDir = File.createTempFile("fpdi-document-export", ".pdf").getParentFile();
	} else {
		outputDir = new File(storagePath);
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs()) {
				throw new RuntimeException("Failed to create storage directory.");
			}
		} else if (!outputDir.isDirectory()) {
			throw new RuntimeException("The configured storage path is not a directory.");
		}
	}
	
	final String prefix = String.format("fpdi-doc-export-%d", System.currentTimeMillis());

	File coverPdfFile = new File(outputDir, String.format("%s-cover.pdf", prefix));
	File preTextPdfFile = new File(outputDir, String.format("%s-pre-text.pdf", prefix));
	File summaryPdfFile = new File(outputDir, String.format("%s-summary.pdf", prefix));
	File finalSummaryPdfFile = new File(outputDir, String.format("%s-final-summary.pdf", prefix));
	File contentFile = new File(outputDir, String.format("%s-content.pdf", prefix));
	File destinationFile = new File(outputDir, String.format("%s-mounted.pdf", prefix));
	File finalPdfFile = new File(outputDir, String.format("%s-final.pdf", prefix));

	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(contentFile));
	PdfWriter coverWriter = PdfWriter.getInstance(coverDocument, new FileOutputStream(coverPdfFile));
	PdfWriter.getInstance(preTextDocument, new FileOutputStream(preTextPdfFile));
	PdfWriter.getInstance(summaryDocument, new FileOutputStream(summaryPdfFile));

	TOCEvent event = new TOCEvent();
	writer.setPageEvent(event);

	// DEFINIÇÕES DE FONTE, MARGENS, ESPAÇAMENTO E CORES
	Font texto = FontFactory.getFont(FontFactory.TIMES, 12.0f);

	Font titulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 12.0f);
	Font tituloCapa = FontFactory.getFont(FontFactory.TIMES_BOLD, 14.0f);
	// Cor cinza - cabeçalho das tabelas
	CMYKColor headerBgColor = new CMYKColor(55, 45, 42, 7);

	// 0,8 cm acima e abaixo
	float paragraphSpacing = 22.6772f;
	// Parágrafo com 1,25 cm na primeira linha
	float firstLineIndent = 35.43307f;
	// 1,5 entrelinhas
	float interLineSpacing = texto.getCalculatedLeading(1.5f);
	// Formato A4 do documento
	coverDocument.setPageSize(PageSize.A4);
	// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
	coverDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);

	coverDocument.open();

	// CABEÇALHO
	String imageUrl = domain.getCompany().getLogo();
	// LOGGER.info("|"+imageUrl+"|");
	if (!imageUrl.trim().isEmpty()) {
		Image image = Image.getInstance(new URL(imageUrl));
		// image.scaleAbsolute(150f, 150f);
		float scaler = ((coverDocument.getPageSize().getWidth() - coverDocument.leftMargin()
				- coverDocument.rightMargin()) / image.getWidth()) * 100;
		image.scalePercent(scaler * 0.4f);
		image.setAlignment(Element.ALIGN_CENTER);
		coverDocument.add(image);
	}

	Paragraph TITULO = new Paragraph(title, tituloCapa);
	// Paragraph AUTHOR = new Paragraph(author, texto);
	TITULO.setAlignment(Element.ALIGN_CENTER);
	TITULO.setSpacingBefore(paragraphSpacing * 8);

	// AUTHOR.setAlignment(Element.ALIGN_CENTER);

	coverDocument.add(TITULO);
	// document.add(AUTHOR);
	// document.add(YEAR);
	Phrase localizationPhrase = new Phrase(this.domain.getCompany().getLocalization(), titulo);
	// Phrase footer = new Phrase(String.valueOf(cal.get(Calendar.YEAR)),
	// titulo);
	PdfContentByte cb = coverWriter.getDirectContent();
	ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, localizationPhrase,
			(coverDocument.right() - coverDocument.left()) / 2 + coverDocument.leftMargin(),
			coverDocument.bottom() + 30, 0);
	coverDocument.newPage();

	// FOLHA DE ROSTO
	Paragraph COMPANY = new Paragraph(domain.getCompany().getName(), tituloCapa);
	COMPANY.setAlignment(Element.ALIGN_CENTER);
	COMPANY.setSpacingBefore(paragraphSpacing);
	coverDocument.add(COMPANY);

	coverDocument.add(TITULO);

	Calendar cal = Calendar.getInstance();

	Phrase periodPhrase = new Phrase(
			String.valueOf(cal.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cal.get(Calendar.YEAR)), titulo);
	// footerRosto.add(periodPhrase);
	// footerRosto.add(footer);
	ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, periodPhrase,
			(coverDocument.right() - coverDocument.left()) / 2 + coverDocument.leftMargin(),
			coverDocument.bottom() + 15, 0);
	ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, localizationPhrase,
			(coverDocument.right() - coverDocument.left()) / 2 + coverDocument.leftMargin(),
			coverDocument.bottom() + 30, 0);
	coverDocument.newPage();
	coverDocument.close();

	String[] sections = lista.split(",");
	int secIndex = 0, subSecIndex = 0;

	boolean lastAttWasPlan = false;

	document.setPageSize(PageSize.A4);
	// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
	document.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
	document.open();

	preTextDocument.setPageSize(PageSize.A4);
	// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
	preTextDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
	preTextDocument.open();

	boolean havePreText = false;
	boolean haveContent = false;

	for (int i = 0; i < sections.length; i++) {
		DocumentSection ds = this.docBS.retrieveSectionById(Long.parseLong(sections[i]));
		ds.setDocumentAttributes(this.docBS.listAttributesBySection(ds, ds.getDocument().getPlan().getId()));

		subSecIndex = 0;
		String secName = ds.getName();

		if (ds.isPreTextSection()) { // SEÇÕES PRÉ TEXTUAIS

			for (DocumentAttribute a : ds.getDocumentAttributes()) {
				if (a.getType().equals(TableField.class.getCanonicalName())) {
					havePreText = true;
					TableFields tf = fieldsBS.tableFieldsByAttribute(a.getId(), true);
					List<TableStructure> tabStructList = fieldsBS.listTableStructureByFields(tf);
					List<TableInstance> tabInstList = fieldsBS.listTableInstanceByFields(tf);
					if (!tabInstList.isEmpty()) {
						// String attName = a.getName();
						// if (!attName.equals(secName)) {

						Chunk c = new Chunk(secName, titulo);
						c.setGenericTag(secName);

						Paragraph attTitle = new Paragraph(c);
						attTitle.setAlignment(Element.ALIGN_CENTER);
						attTitle.setLeading(interLineSpacing);
						attTitle.setSpacingAfter(paragraphSpacing);
						attTitle.setSpacingBefore(paragraphSpacing);
						preTextDocument.add(attTitle);
						// }
						PdfPTable table;
						if (tabStructList
								.size() == 4) /**
												 * SOLUÇÃO DE CONTORNO PARA
												 * TRATAR O CASO ESPECIAL DO
												 * HISTÓRICO DE VERSÕES QUE
												 * É UMA SEÇÃO PRÉ-TEXTUAL
												 * MAS DEVE EXIBIR BORDA E
												 * CABEÇALHO, NECESSITA DE
												 * MELHORIA
												 */
							table = returnPdfPTable(tabStructList, tabInstList, false, false);
						else
							table = returnPdfPTable(tabStructList, tabInstList, true, true);
						preTextDocument.add(table);
						preTextDocument.newPage();
						lastAttWasPlan = false;
					}
				}
			}

		} else {
			haveContent = true;
			// SEÇÕES NUMERADAS
			/*
			 * if (lastSecWasPreText) { lastSecWasPreText = false;
			 * 
			 * Paragraph secTitle = new Paragraph("Sumário", titulo);
			 * secTitle.setLeading(interLineSpacing);
			 * secTitle.setSpacingAfter(paragraphSpacing);
			 * secTitle.setSpacingBefore(paragraphSpacing);
			 * secTitle.setAlignment(Element.ALIGN_CENTER);
			 * document.add(secTitle); int summaryIndex = 0; int
			 * summarySubSecIndex = 0; for (String secaoId : sections) {
			 * summarySection =
			 * this.retrieveSectionById(Long.parseLong(secaoId)); if
			 * (!summarySection.isPreTextSection()) {
			 * 
			 * summaryIndex++; secTitle = new Paragraph(summaryIndex + ". "
			 * + summarySection.getName(), titulo);
			 * secTitle.setLeading(interLineSpacing);
			 * document.add(secTitle);
			 * 
			 * List<DocumentSection> dsList =
			 * this.listSectionsSons(summarySection);
			 * this.setSectionsFilled(dsList,
			 * summarySection.getDocument().getPlan().getId());
			 * summarySubSecIndex = 0; for (DocumentSection sec : dsList) {
			 * 
			 * if (sec.isFilled()) { summarySubSecIndex++; secTitle = new
			 * Paragraph( summaryIndex + "." + summarySubSecIndex + ". " +
			 * sec.getName(), texto); secTitle.setLeading(interLineSpacing);
			 * secTitle.setFirstLineIndent(firstLineIndent / 2);
			 * document.add(secTitle); } }
			 * 
			 * } } document.newPage(); }
			 */

			secIndex++;

			// LOGGER.info(ds.getId() + ". " + ds.getName() + " - Size:
			// " +
			// ds.getDocumentAttributes().size());

			boolean secTitlePrinted = false;

			if (ds.getDocumentAttributes().size() == 0) {
				if (lastAttWasPlan) {
					document.setPageSize(PageSize.A4);
					document.newPage();
				}
				
				Chunk c = new Chunk(secIndex + ". " + secName, titulo);
				c.setGenericTag(secIndex + ". " + secName);
				Paragraph secTitle = new Paragraph(c);
				secTitle.setLeading(interLineSpacing);
				secTitle.setSpacingAfter(paragraphSpacing);
				secTitle.setSpacingBefore(paragraphSpacing);
				document.add(secTitle);
				secTitlePrinted = true;
				lastAttWasPlan = false;
			}

			for (DocumentAttribute a : ds.getDocumentAttributes()) {
				if (a.getType().equals(TextArea.class.getCanonicalName())) {
					if (a.getValue() != null && !a.getValue().equals("")) {
						if (lastAttWasPlan) {
							document.setPageSize(PageSize.A4);
							document.newPage();
						}
						if (!secTitlePrinted) {
							Chunk c = new Chunk(secIndex + ". " + secName, titulo);
							c.setGenericTag(secIndex + ". " + secName);
							Paragraph secTitle = new Paragraph(c);
							secTitle.setLeading(interLineSpacing);
							secTitle.setSpacingAfter(paragraphSpacing);
							secTitle.setSpacingBefore(paragraphSpacing);
							document.add(secTitle);
							secTitlePrinted = true;
						}
						String attName = a.getName();
						if (!attName.equals(secName)) {
							Paragraph attTitle = new Paragraph(attName, titulo);
							attTitle.setLeading(interLineSpacing);
							attTitle.setSpacingAfter(paragraphSpacing);
							attTitle.setSpacingBefore(paragraphSpacing);
							document.add(attTitle);
						}
						// HTMLWorker htmlWorker = new
						// HTMLWorker(document);
						Map<String, String> pc2 = new HashMap<String, String>();
						pc2.put("line-height", "115%");
						pc2.put("margin-bottom", "6.0pt");
						pc2.put("text-align", "center");

						HashMap<String, String> spanc1 = new HashMap<String, String>();
						spanc1.put("text-justify", "inter-word");

						StyleSheet styles = new StyleSheet();
						styles.loadTagStyle("p", "text-indent", "1.25cm");

						String str = "<html>" + "<head>" + "</head><body style=\"text-indent: 1.25cm; \">"
								+ "<p style=\"text-indent: 1.25cm; \">";
						Queue<String> allMatches = new LinkedList<>();
						String value = a.getValue();
						if (a.getValue().contains("<img")) {
							Matcher m = Pattern.compile("<img [^>]*>").matcher(a.getValue());
							while (m.find()) {
								String match = m.group();
								allMatches.add(match);
								value = value.replace(match, "<p>||IMAGE||</p>");
							}
						}
						str += value + "</p></body></html>";

						File htmlFile = new File(outputDir, String.format("%s-1.html", prefix));
						FileWriter fw = new FileWriter(htmlFile, true);
						BufferedWriter conexao = new BufferedWriter(fw);
						conexao.write(str);
						conexao.newLine();
						conexao.close();
						// LOGGER.info(htmlFile.getPath());

						List<Element> p = HTMLWorker.parseToList(new FileReader(htmlFile.getPath()), styles);
						for (int k = 0; k < p.size(); ++k) {
							if (p.get(k) instanceof Paragraph) {
								Paragraph att = (Paragraph) p.get(k);
								// LOGGER.info("------->"+att.getContent());
								if (att.getContent().contains("||IMAGE||")) {
									String img = allMatches.poll();
									if (img != null) {
										// LOGGER.info("IMG------->"+img);
										Image image = Image.getInstance(
												new URL(img.replaceAll("<img src=\"", "").replaceAll("\">", "")));
										float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
												- document.rightMargin()) / image.getWidth()) * 100;
										image.scalePercent(scaler * 0.4f);
										image.setAlignment(Element.ALIGN_CENTER);
										document.add(image);
									}
								} else {
									att.setFirstLineIndent(firstLineIndent);
									document.add(att);
								}
							} else if (p.get(k).getClass().getName().equals("com.itextpdf.text.List")) {
								com.itextpdf.text.List att = (com.itextpdf.text.List) p.get(k);
								att.setIndentationLeft(firstLineIndent);
								document.add(att);
							}
						}
						lastAttWasPlan = false;
						htmlFile.delete();
					}
				} else if (a.getType().equals(TableField.class.getCanonicalName())) {

					TableFields tf = fieldsBS.tableFieldsByAttribute(a.getId(), true);
					List<TableStructure> tabStructList = fieldsBS.listTableStructureByFields(tf);
					List<TableInstance> tabInstList = fieldsBS.listTableInstanceByFields(tf);
					if (!tabInstList.isEmpty()) {
						if (lastAttWasPlan) {
							document.setPageSize(PageSize.A4);
							document.newPage();
						}
						if (!secTitlePrinted) {
							Chunk c = new Chunk(secIndex + ". " + secName, titulo);
							c.setGenericTag(secIndex + ". " + secName);
							Paragraph secTitle = new Paragraph(c);
							secTitle.setLeading(interLineSpacing);
							secTitle.setSpacingAfter(paragraphSpacing);
							secTitle.setSpacingBefore(paragraphSpacing);
							document.add(secTitle);
							secTitlePrinted = true;
						}
						String attName = a.getName();
						if (!attName.equals(secName)) {
							Paragraph attTitle = new Paragraph(attName, texto);
							attTitle.setAlignment(Element.ALIGN_CENTER);
							attTitle.setLeading(interLineSpacing);
							attTitle.setSpacingAfter(paragraphSpacing);
							attTitle.setSpacingBefore(paragraphSpacing);
							document.add(attTitle);
						}
						PdfPTable table = returnPdfPTable(tabStructList, tabInstList, false, false);
						document.add(table);
						lastAttWasPlan = false;
					}

				} else if (a.getType().equals(ScheduleField.class.getCanonicalName())) {
					List<ScheduleInstance> schInstList = this.fieldsBS
							.retrieveScheduleInstanceByAttribute(a.getId(), true);
					if (!schInstList.isEmpty()) {
						if (lastAttWasPlan) {
							document.setPageSize(PageSize.A4);
							document.newPage();
						}
						if (!secTitlePrinted) {
							Chunk c = new Chunk(secIndex + ". " + secName, titulo);
							c.setGenericTag(secIndex + ". " + secName);
							Paragraph secTitle = new Paragraph(c);
							secTitle.setLeading(interLineSpacing);
							secTitle.setSpacingAfter(paragraphSpacing);
							secTitle.setSpacingBefore(paragraphSpacing);
							document.add(secTitle);
							secTitlePrinted = true;
						}
						String attName = a.getName();
						if (!attName.equals(secName)) {
							Paragraph attTitle = new Paragraph(attName, texto);
							attTitle.setAlignment(Element.ALIGN_CENTER);
							attTitle.setLeading(interLineSpacing);
							attTitle.setSpacingAfter(paragraphSpacing);
							attTitle.setSpacingBefore(paragraphSpacing);
							document.add(attTitle);
						}
						PdfPTable table = new PdfPTable(3);
						table.getDefaultCell();
						PdfPCell c = new PdfPCell(new Paragraph("Atividade", texto));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(headerBgColor);
						table.addCell(c);
						c = new PdfPCell(new Paragraph("Início", texto));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(headerBgColor);

						table.addCell(c);
						c = new PdfPCell(new Paragraph("Fim", texto));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(headerBgColor);
						table.addCell(c);

						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						for (ScheduleInstance sch : schInstList) {
							table.addCell(new Paragraph(sch.getDescription(), texto));
							table.addCell(new Paragraph(sdf.format(sch.getBegin()), texto));
							table.addCell(new Paragraph(sdf.format(sch.getEnd()), texto));
						}
						document.add(table);
						lastAttWasPlan = false;
					}
				} else if (a.getType().equals(SelectPlan.class.getCanonicalName())) {
					if (a.getValue() != null) {
						Plan plan = planBS.retrieveById(Long.parseLong(a.getValue()));
						List<PdfPTable> planTableList = this.generatePDFplanTable(plan);
						boolean first = true;
						// LOGGER.info("2 - " + plan.getName());
						// LOGGER.info(secName + " " + secTitlePrinted);
						for (PdfPTable planTable : planTableList) {
							if (!lastAttWasPlan) {
								document.setPageSize(PageSize.A4.rotate());
							}
							document.newPage();
							if (first) {

								if (!secTitlePrinted) {
									Chunk c = new Chunk(secIndex + ". " + secName, titulo);
									c.setGenericTag(secIndex + ". " + secName);
									Paragraph secTitle = new Paragraph(c);
									document.add(secTitle);
									secTitlePrinted = true;
								}
								Paragraph attTitle = new Paragraph(plan.getName(), texto);
								attTitle.setAlignment(Element.ALIGN_CENTER);
								document.add(attTitle);
								first = false;
								lastAttWasPlan = true;
							}
							document.add(planTable);
						}
						// document.setPageSize(PageSize.A4);
						// document.newPage();
					}
				}
			}
			
			List<DocumentSection> dsList = this.docBS.listSectionsSons(ds);
			this.docBS.setSectionsFilled(dsList, ds.getDocument().getPlan().getId());

			//subitens de um item
			for (DocumentSection d : dsList) {
				if (d.isFilled()) {
					// LOGGER.info("filled: " + d.getName());
					String subSecName = d.getName();
					boolean subSecTitlePrinted = false;
					// if (!secName.equals(subSecName)) {
					/*
					 * subSecIndex++; Paragraph subSecTitle = new
					 * Paragraph(secIndex + "." + subSecIndex + ". " +
					 * subSecName, titulo);
					 * subSecTitle.setLeading(interLineSpacing);
					 * subSecTitle.setSpacingAfter(paragraphSpacing);
					 * subSecTitle.setSpacingBefore(paragraphSpacing);
					 * document.add(subSecTitle); subSecTitlePrinted = true;
					 */
					// }

					List<DocumentAttribute> attList = this.docBS.listAttributesBySection(d,
							d.getDocument().getPlan().getId());
					// LOGGER.info("attList.size: " + attList.size());
					for (DocumentAttribute a : attList) {
						if (a.getType().equals(TextArea.class.getCanonicalName())) {
							if (a.getValue() != null && !a.getValue().equals("")) {
								String attName = a.getName();
								if (lastAttWasPlan) {
									document.setPageSize(PageSize.A4);
									document.newPage();
								}
								if (!subSecTitlePrinted) {
									subSecIndex++;
									Chunk c = new Chunk(secIndex + "." + subSecIndex + ". " + subSecName, titulo);
									c.setGenericTag(secIndex + "." + subSecIndex + ". " + subSecName);
									Paragraph subSecTitle = new Paragraph(c);
									subSecTitle.setLeading(interLineSpacing);
									subSecTitle.setSpacingAfter(paragraphSpacing);
									subSecTitle.setSpacingBefore(paragraphSpacing);
									document.add(subSecTitle);
									subSecTitlePrinted = true;
								}
								if (!attName.equals(subSecName)) {
									Paragraph attTitle = new Paragraph(attName, titulo);
									attTitle.setLeading(interLineSpacing);
									attTitle.setSpacingAfter(paragraphSpacing);
									attTitle.setSpacingBefore(paragraphSpacing);
									document.add(attTitle);
								}
								// HTMLWorker htmlWorker = new
								// HTMLWorker(document);
								Map<String, String> pc2 = new HashMap<String, String>();
								pc2.put("line-height", "115%");
								pc2.put("margin-bottom", "6.0pt");
								pc2.put("text-align", "center");

								HashMap<String, String> spanc1 = new HashMap<String, String>();
								spanc1.put("text-justify", "inter-word");

								StyleSheet styles = new StyleSheet();
								styles.loadTagStyle("p", "text-indent", "1.25cm");

								String str = "<html>" + "<head>"
										+ "</head><body style=\"text-indent: 1.25cm; font-size: 12pt !important;\">"
										+ "<p style=\"text-indent: 1.25cm; font-size: 12pt !important;\">";
								Queue<String> allMatches = new LinkedList<>();
								String value = a.getValue();
								if (a.getValue().contains("<img")) {
									Matcher m = Pattern.compile("<img [^>]*>").matcher(a.getValue());
									while (m.find()) {
										String match = m.group();
										allMatches.add(match);
										value = value.replace(match, "<p>||IMAGE||</p>");
									}
								}
								str += value + "</p></body></html>";

								File htmlFile = new File(outputDir, String.format("%s-2.html", prefix));
								FileWriter fw = new FileWriter(htmlFile, true);
								BufferedWriter conexao = new BufferedWriter(fw);
								conexao.write(str);
								conexao.newLine();
								conexao.close();

								List<Element> p = HTMLWorker.parseToList(new FileReader(htmlFile.getPath()),
										styles);

								for (int k = 0; k < p.size(); ++k) {
									if (p.get(k) instanceof Paragraph) {
										Paragraph att = (Paragraph) p.get(k);
										if (att.getContent().contains("||IMAGE||")) {
											String img = allMatches.poll();
											if (img != null) {
												Image image = Image.getInstance(new URL(
														img.replaceAll("<img src=\"", "").replaceAll("\">", "")));
												float scaler = ((document.getPageSize().getWidth()
														- document.leftMargin() - document.rightMargin())
														/ image.getWidth()) * 100;
												image.scalePercent(scaler * 0.4f);
												image.setAlignment(Element.ALIGN_CENTER);
												document.add(image);
											}
										} else {
											att.setFirstLineIndent(firstLineIndent);
											document.add(att);

										}
									} else if (p.get(k).getClass().getName().equals("com.itextpdf.text.List")) {
										com.itextpdf.text.List att = (com.itextpdf.text.List) p.get(k);
										att.setIndentationLeft(firstLineIndent);
										document.add(att);
									}
								}
								lastAttWasPlan = false;
								htmlFile.delete();
							}
						} else if (a.getType().equals(TableField.class.getCanonicalName())) {

							TableFields tf = fieldsBS.tableFieldsByAttribute(a.getId(), true);
							List<TableStructure> tabStructList = fieldsBS.listTableStructureByFields(tf);
							List<TableInstance> tabInstList = fieldsBS.listTableInstanceByFields(tf);
							if (!tabInstList.isEmpty()) {
								if (lastAttWasPlan) {
									document.setPageSize(PageSize.A4);
									document.newPage();
								}
								String attName = a.getName();
								if (!subSecTitlePrinted) {
									subSecIndex++;
									Chunk c = new Chunk(secIndex + "." + subSecIndex + ". " + subSecName, titulo);
									c.setGenericTag(secIndex + "." + subSecIndex + ". " + subSecName);
									Paragraph subSecTitle = new Paragraph(c);
									subSecTitle.setLeading(interLineSpacing);
									subSecTitle.setSpacingAfter(paragraphSpacing);
									subSecTitle.setSpacingBefore(paragraphSpacing);
									document.add(subSecTitle);
									subSecTitlePrinted = true;
								}
								if (!attName.equals(subSecName)) {
									Paragraph attTitle = new Paragraph(attName, texto);
									attTitle.setAlignment(Element.ALIGN_CENTER);
									attTitle.setLeading(interLineSpacing);
									attTitle.setSpacingAfter(paragraphSpacing);
									attTitle.setSpacingBefore(paragraphSpacing);
									document.add(attTitle);
								}
								PdfPTable table = returnPdfPTable(tabStructList, tabInstList, false, false);
								document.add(table);
								lastAttWasPlan = false;
							}
						} else if (a.getType().equals(ScheduleField.class.getCanonicalName())) {
							List<ScheduleInstance> schInstList = this.fieldsBS
									.retrieveScheduleInstanceByAttribute(a.getId(), true);

							if (!schInstList.isEmpty()) {
								if (lastAttWasPlan) {
									document.setPageSize(PageSize.A4);
									document.newPage();
								}
								if (!subSecTitlePrinted) {
									subSecIndex++;
									Chunk c = new Chunk(secIndex + "." + subSecIndex + ". " + subSecName, titulo);
									c.setGenericTag(secIndex + "." + subSecIndex + ". " + subSecName);
									Paragraph subSecTitle = new Paragraph(c);
									subSecTitle.setLeading(interLineSpacing);
									subSecTitle.setSpacingAfter(paragraphSpacing);
									subSecTitle.setSpacingBefore(paragraphSpacing);
									document.add(subSecTitle);
									subSecTitlePrinted = true;
								}
								String attName = a.getName();
								if (!attName.equals(subSecName)) {
									Paragraph attTitle = new Paragraph(attName, texto);
									attTitle.setAlignment(Element.ALIGN_CENTER);
									attTitle.setLeading(interLineSpacing);
									attTitle.setSpacingAfter(paragraphSpacing);
									attTitle.setSpacingBefore(paragraphSpacing);
									document.add(attTitle);
								}
								PdfPTable table = new PdfPTable(3);
								table.getDefaultCell();
								PdfPCell c = new PdfPCell(new Paragraph("Atividade", texto));
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								c.setBackgroundColor(headerBgColor);
								table.addCell(c);
								c = new PdfPCell(new Paragraph("Início", texto));
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								c.setBackgroundColor(headerBgColor);

								table.addCell(c);
								c = new PdfPCell(new Paragraph("Fim", texto));
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								c.setBackgroundColor(headerBgColor);
								table.addCell(c);

								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								for (ScheduleInstance sch : schInstList) {
									table.addCell(new Paragraph(sch.getDescription(), texto));
									table.addCell(new Paragraph(sdf.format(sch.getBegin()), texto));
									table.addCell(new Paragraph(sdf.format(sch.getEnd()), texto));
								}
								document.add(table);
								lastAttWasPlan = false;
							}
						} else if (a.getType().equals(SelectPlan.class.getCanonicalName())) {
							if (a.getValue() != null) {
								Plan plan = planBS.retrieveById(Long.parseLong(a.getValue()));
								// LOGGER.info("2 - " + plan.getName());
								List<PdfPTable> planTableList = this.generatePDFplanTable(plan);
								boolean first = true;
								for (PdfPTable planTable : planTableList) {
									if (!lastAttWasPlan) {
										document.setPageSize(PageSize.A4.rotate());
									}
									document.newPage();
									if (first) {
										if (!subSecTitlePrinted) {
											subSecIndex++;
											Chunk c = new Chunk(secIndex + "." + subSecIndex + ". " + subSecName,
													titulo);
											c.setGenericTag(secIndex + "." + subSecIndex + ". " + subSecName);
											Paragraph subSecTitle = new Paragraph(c);
											document.add(subSecTitle);
											subSecTitlePrinted = true;
										}
										Paragraph attTitle = new Paragraph(plan.getName(), texto);
										attTitle.setAlignment(Element.ALIGN_CENTER);
										document.add(attTitle);
										first = false;
										lastAttWasPlan = true;
									}
									document.add(planTable);
								}
								// document.setPageSize(PageSize.A4);
								// document.newPage();
							}
						}
					}
				}
			}
		}
	}
	if (havePreText)
		preTextDocument.close();
	if (haveContent) {
		if(document.getPageNumber()>0) {
			document.close();
		}
	}
	summaryDocument.setPageSize(PageSize.A4);
	// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
	summaryDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
	summaryDocument.open();

	Paragraph summaryTitle = new Paragraph("Sumário", titulo);
	summaryTitle.setLeading(interLineSpacing);
	summaryTitle.setSpacingAfter(paragraphSpacing);
	summaryTitle.setSpacingBefore(paragraphSpacing);
	summaryDocument.add(summaryTitle);

	Chunk dottedLine = new Chunk(new DottedLineSeparator());
	List<SimpleEntry<String, SimpleEntry<String, Integer>>> entries = event.getTOC();
	Paragraph p;
	int summaryCountPages = 0;
	for (SimpleEntry<String, SimpleEntry<String, Integer>> entry : entries) {
		// LOGGER.info(entry.getKey());
		Chunk chunk = new Chunk(entry.getKey(), titulo);
		SimpleEntry<String, Integer> value = entry.getValue();
		chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
		p = new Paragraph(chunk);
		p.add(dottedLine);
		chunk = new Chunk(String.valueOf(value.getValue()), titulo);
		chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
		p.add(chunk);
		summaryDocument.add(p);
	}
	summaryDocument.close();
	PdfReader summaryAux = new PdfReader(summaryPdfFile.getPath());
	PdfReader cover = new PdfReader(coverPdfFile.getPath());
	summaryCountPages = summaryAux.getNumberOfPages() + cover.getNumberOfPages();
	PdfReader preText;
	if (havePreText) {
		preText = new PdfReader(preTextPdfFile.getPath());
		summaryCountPages += preText.getNumberOfPages();
	}

	com.itextpdf.text.Document finalSummaryDocument = new com.itextpdf.text.Document();
	PdfWriter.getInstance(finalSummaryDocument, new FileOutputStream(finalSummaryPdfFile));
	// Formato A4 do documento
	finalSummaryDocument.setPageSize(PageSize.A4);
	// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
	finalSummaryDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
	finalSummaryDocument.open();

	finalSummaryDocument.add(summaryTitle);
	for (SimpleEntry<String, SimpleEntry<String, Integer>> entry : entries) {
		// LOGGER.info(entry.getKey());
		Chunk chunk = new Chunk(entry.getKey(), titulo);
		SimpleEntry<String, Integer> value = entry.getValue();
		chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
		p = new Paragraph(chunk);
		p.add(dottedLine);
		chunk = new Chunk(String.valueOf(value.getValue() + summaryCountPages), titulo);
		chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
		p.add(chunk);
		finalSummaryDocument.add(p);
	}
	finalSummaryDocument.close();

	com.itextpdf.text.Document newDocument = new com.itextpdf.text.Document();

	PdfImportedPage page;
	int n;
	PdfCopy copy = new PdfCopy(newDocument, new FileOutputStream(destinationFile.getPath()));
	newDocument.open();

	PdfReader summary = new PdfReader(finalSummaryPdfFile.getPath());
	PdfReader content;
	// int unnumberedPgsCount = summaryCountPages;
	// CAPA
	n = cover.getNumberOfPages();
	// unnumberedPgsCount += n;
	for (int i = 0; i < n;) {
		page = copy.getImportedPage(cover, ++i);
		copy.addPage(page);
	}
	if (havePreText) {
		preText = new PdfReader(preTextPdfFile.getPath());
		// SEÇÕES PRE TEXTUAIS
		n = preText.getNumberOfPages();
		// unnumberedPgsCount += n;
		for (int i = 0; i < n;) {
			page = copy.getImportedPage(preText, ++i);
			copy.addPage(page);
		}
	}
	if (haveContent) {
		// SUMÁRIO
		n = summary.getNumberOfPages();
		for (int i = 0; i < n;) {
			page = copy.getImportedPage(summary, ++i);
			copy.addPage(page);
		}
		content = new PdfReader(contentFile.getPath());
		// CONTEÚDO
		n = content.getNumberOfPages();
		for (int i = 0; i < n;) {
			page = copy.getImportedPage(content, ++i);
			copy.addPage(page);
		}
	}

	newDocument.close();

	manipulatePdf(destinationFile.getPath(), finalPdfFile.getPath(), newDocument, summaryCountPages);
	InputStream inpStr = new FileInputStream(finalPdfFile);
	return inpStr;
}

/**
 * Gera tabelas PDF do plano de meta passado por parâmetro.
 * 
 * @param plan
 *            Plano de metas para geração de PDF.
 * @return List<PdfPTable> Lista de tabelas em PDF.
 */
public List<PdfPTable> generatePDFplanTable(Plan plan) {

	CMYKColor eixoHeaderBgColor = new CMYKColor(0, 0, 0, 70);
	// Cor cinza - cabeçalho objetivo do plano de metas
	CMYKColor objetivoHeaderBgColor = new CMYKColor(0, 0, 0, 50);
	// Cor cinza - conteudo objetivo do plano de metas
	CMYKColor objetivoRowBgColor = new CMYKColor(0, 0, 0, 20);
	// Cor branco - borda tabela plano de metas
	CMYKColor borderPlanColor = new CMYKColor(0, 0, 0, 0);
	// Cor roxo - cabeçalho indicador do plano de metas
	CMYKColor indicadorHeaderBgColor = new CMYKColor(62, 30, 0, 18);
	// Cor roxo - conteudo indicador do plano de metas
	CMYKColor indicadorRowBgColor = new CMYKColor(28, 14, 0, 9);
	Font texto = FontFactory.getFont(FontFactory.TIMES, 12.0f);
	Font titulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 12.0f);
	float paragraphSpacing = 22.6772f;
	PdfPTable table = new PdfPTable(5);
	// Plan plan = planBS.retrieveById(planId);
	List<StructureLevelInstance> list = structureBS.listRootLevelInstanceByPlan(plan);
	// ArrayList<Long> attInstList = new ArrayList<Long>();
	ArrayList<PdfPTable> tableList = new ArrayList<PdfPTable>();
	for (StructureLevelInstance s : list) {

		PaginatedList<StructureLevelInstance> structureLevelSons = new PaginatedList<>();
		structureLevelSons.setList(structureBS.retrieveLevelInstanceSons(s.getId()));
		s.setSons(structureLevelSons);

		List<Attribute> attributeList = structureBS.retrieveLevelAttributes(s.getLevel());
		attributeList = structureBS.setAttributesInstances(s, attributeList);

		if (!s.getSons().getList().isEmpty()) {
			for (StructureLevelInstance son : s.getSons().getList()) {
				if (son.getLevel().isObjective()) {// Objetivo
					String eixoLabel = s.getLevel().getName() + ": ";
					String eixoName = s.getName();
					table = new PdfPTable(5);
					table.setSpacingBefore(paragraphSpacing);
					table.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.setWidthPercentage(100.0f);

					Phrase phraseEixoLabel = new Phrase(eixoLabel, titulo);
					Phrase phraseEixoName = new Phrase(eixoName, texto);
					phraseEixoLabel.add(phraseEixoName);

					PdfPCell cell = new PdfPCell(phraseEixoLabel);

					cell.setHorizontalAlignment(Element.ALIGN_CENTER);

					// centraliza verticalmente
					Float fontSize = titulo.getSize();
					Float capHeight = titulo.getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);
					Float padding = 5f;
					cell.setPadding(padding);
					cell.setPaddingTop(capHeight - fontSize + padding);

					cell.setBackgroundColor(eixoHeaderBgColor);
					cell.setBorderColor(borderPlanColor);
					cell.setColspan(5);
					table.addCell(cell);

					String objetivoLabel = son.getLevel().getName() + ": ";
					String objetivoName = son.getName();
					Phrase phraseObjetivoLabel = new Phrase(objetivoLabel, titulo);
					Phrase phraseObjetivoName = new Phrase(objetivoName, texto);
					phraseObjetivoLabel.add(phraseObjetivoName);

					cell = new PdfPCell(phraseObjetivoLabel);

					cell.setHorizontalAlignment(Element.ALIGN_CENTER);

					// centraliza verticalmente
					cell.setPadding(padding);
					cell.setPaddingTop(capHeight - fontSize + padding);

					cell.setBackgroundColor(objetivoHeaderBgColor);
					cell.setBorderColor(borderPlanColor);
					cell.setColspan(5);
					table.addCell(cell);

					List<BudgetDTO> budgetList = new ArrayList<BudgetDTO>();
					int budgetListSize = 0;

					List<Attribute> sonAttributeList = structureBS.retrieveLevelAttributes(son.getLevel());
					sonAttributeList = structureBS.setAttributesInstances(son, sonAttributeList);
					if (!sonAttributeList.isEmpty()) {
						String bsc = "-";
						for (Attribute sonAttribute : sonAttributeList) {

							if (sonAttribute.isBscField()) { // Perspectiva
								AttributeInstance attInst = attrHelper.retrieveAttributeInstance(son, sonAttribute);
								if (attInst != null) {
									bsc = attInst.getValue();
								} else {
									bsc = "-";
								}
							}
							if (sonAttribute.getBudgets() != null && !sonAttribute.getBudgets().isEmpty()) { // Orçamento
								budgetList = sonAttribute.getBudgets();
								budgetListSize = budgetList.size();
							}
						}
						if (budgetListSize > 0) {
							// Perspectiva BSC
							cell = new PdfPCell(new Phrase("Perspectiva do BSC", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(objetivoHeaderBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(1);
							table.addCell(cell);

							// Orçamento
							cell = new PdfPCell(new Phrase("Orçamento", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(objetivoHeaderBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(4);
							table.addCell(cell);

							// Perspectiva BSC - valor
							cell = new PdfPCell(new Phrase(bsc, texto));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							cell.setBackgroundColor(objetivoRowBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(1);
							cell.setRowspan(budgetListSize + 1);
							table.addCell(cell);

							// Subação
							cell = new PdfPCell(new Phrase("Subação", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(objetivoRowBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(1);
							table.addCell(cell);

							// Planejado
							cell = new PdfPCell(new Phrase("Planejado", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(objetivoRowBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(1);
							table.addCell(cell);

							// Empenhado
							cell = new PdfPCell(new Phrase("Empenhado", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(objetivoRowBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(1);
							table.addCell(cell);

							// Realizado
							cell = new PdfPCell(new Phrase("Realizado", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(objetivoRowBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(1);
							table.addCell(cell);

							for (BudgetDTO b : budgetList) {
								// Subação - valor
								cell = new PdfPCell(new Phrase(b.getBudget().getSubAction(), texto));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								table.addCell(cell);

								// Para formatar valores em R$
								Locale ptBr = new Locale("pt", "BR"); // Locale
																		// para
																		// o
																		// Brasil
								NumberFormat moedaFormat = NumberFormat.getCurrencyInstance(ptBr);

								// Planejado - valor
								cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudgetLoa()), texto));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								table.addCell(cell);

								// Empenhado - valor
								cell = new PdfPCell(
										new Phrase(moedaFormat.format(b.getBudget().getCommitted()), texto));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								table.addCell(cell);

								// Realizado - valor
								cell = new PdfPCell(
										new Phrase(moedaFormat.format(b.getBudget().getRealized()), texto));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								table.addCell(cell);
							}
						}
					}

					StructureLevelInstance sonAux = son;
					PaginatedList<StructureLevelInstance> objSonsList = new PaginatedList<>();
					objSonsList.setList(structureBS.retrieveLevelInstanceSons(sonAux.getId()));
					sonAux.setSons(objSonsList);
					objSonsList = sonAux.getSons();

					if (objSonsList.getList().size() > 0) {
						// Indicadores
						cell = new PdfPCell(new Phrase("Indicadores", titulo));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						// centraliza verticalmente
						cell.setPadding(padding);
						cell.setPaddingTop(capHeight - fontSize + padding);
						cell.setBackgroundColor(indicadorHeaderBgColor);
						cell.setBorderColor(borderPlanColor);
						cell.setColspan(1);
						table.addCell(cell);

						// Metas
						cell = new PdfPCell(new Phrase("Metas", titulo));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						// centraliza verticalmente
						cell.setPadding(padding);
						cell.setPaddingTop(capHeight - fontSize + padding);
						cell.setBackgroundColor(objetivoHeaderBgColor);
						cell.setBorderColor(borderPlanColor);
						cell.setColspan(2);
						table.addCell(cell);

						// Esperado
						cell = new PdfPCell(new Phrase("Esperado", titulo));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						// centraliza verticalmente
						cell.setPadding(padding);
						cell.setPaddingTop(capHeight - fontSize + padding);
						cell.setBackgroundColor(objetivoHeaderBgColor);
						cell.setBorderColor(borderPlanColor);
						cell.setColspan(1);
						table.addCell(cell);

						// Alcançado
						cell = new PdfPCell(new Phrase("Alcançado", titulo));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						// centraliza verticalmente
						cell.setPadding(padding);
						cell.setPaddingTop(capHeight - fontSize + padding);
						cell.setBackgroundColor(objetivoHeaderBgColor);
						cell.setBorderColor(borderPlanColor);
						cell.setColspan(1);
						table.addCell(cell);

						// String indicadorName = "";
						String calculo = "";
						Long responsavel = (long) -1;

						for (StructureLevelInstance indicatorSon : objSonsList.getList()) { // Indicadores
							if (indicatorSon.getLevel().isIndicator()) {
								List<Attribute> indicatorSonAttributeList = structureBS
										.retrieveLevelAttributes(indicatorSon.getLevel());
								indicatorSonAttributeList = structureBS.setAttributesInstances(indicatorSon,
										indicatorSonAttributeList);
								indicatorSon.getLevel().setAttributes(indicatorSonAttributeList);
								for (Attribute indicatorSonAttribute : indicatorSonAttributeList) {
									if (indicatorSonAttribute.getId() == 14) { // Cálculo
										AttributeInstance attInst = attrHelper.retrieveAttributeInstance(
												indicatorSon, structureBS.retrieveAttribute((long) 14));
										if (attInst != null) {
											calculo = indicatorSonAttribute.getAttributeInstance().getValue();
										} else {
											calculo = "-";
										}
									}
									if (indicatorSonAttribute.getId() == 7) { // responsável
										AttributeInstance attInst = attrHelper.retrieveAttributeInstance(
												indicatorSon, structureBS.retrieveAttribute((long) 7));
										if (attInst != null) {
											responsavel = Long.parseLong(
													indicatorSonAttribute.getAttributeInstance().getValue());
										} else {
											responsavel = (long) -1;
										}

									}
								}
								// LOGGER.info(calculo);
								// LOGGER.info(responsavel);
								PaginatedList<StructureLevelInstance> levelInstances = new PaginatedList<>();
								levelInstances
										.setList(this.structureBS.retrieveLevelInstanceSons(indicatorSon.getId()));

								// Indicador - valores

								User responsible = userBS.existsByUser(responsavel);
								// LOGGER.info(responsible.toString());
								Phrase indicador = new Phrase(indicatorSon.getName(), texto);
								indicador.add(new Phrase("\n\nCálculo: ", titulo));
								indicador.add(new Phrase(calculo, texto));
								indicador.add(new Phrase("\nResponsável: ", titulo));
								if (responsible != null) {
									indicador.add(new Phrase(responsible.getName(), texto));
								} else {
									indicador.add(new Phrase("-", texto));
								}

								cell = new PdfPCell(indicador);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell.setBackgroundColor(indicadorRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								cell.setMinimumHeight(4 * fontSize);
								if (levelInstances.getList().size() > 0) {
									cell.setRowspan(levelInstances.getList().size());
								}
								table.addCell(cell);

								for (int index = 0; index < levelInstances.getList().size(); index++) {

									levelInstances.getList().get(index).getLevel().setAttributes(this.structureBS
											.retrieveLevelSonsAttributes(levelInstances.getList().get(index)));
								}

								StructureLevelInstance levelInstanceAux = new StructureLevelInstance();
								levelInstanceAux.setId(indicatorSon.getId());
								levelInstanceAux.setSons(levelInstances);

								// CABEÇALHO
								// METAS
								if (levelInstanceAux.getSons().getList().size() != 0) {
									ArrayList<String> expected = new ArrayList<String>();
									ArrayList<String> reached = new ArrayList<String>();

									HashMap<Long, ArrayList<String>> meta = new HashMap<Long, ArrayList<String>>();
									AttributeInstance formatAttr = this.attrHelper
											.retrieveFormatAttributeInstance(levelInstanceAux);
									FormatValue formatValue = FormatValue.forAttributeInstance(formatAttr);
									for (int goalIndex = 0; goalIndex < levelInstanceAux.getSons().getList()
											.size(); goalIndex++) {

										ArrayList<String> values = new ArrayList<String>();
										// LOGGER.info(levelInstanceAux.getSons().get(goalIndex).toString());

										List<AttributeInstance> attInst = structureBS.listAttributeInstanceByLevel(
												levelInstanceAux.getSons().getList().get(goalIndex), false);
										List<Attribute> attList = structureBS.listAttributesPDF(
												levelInstanceAux.getSons().getList().get(goalIndex).getLevel());

										for (Attribute a : attList) {
											if (a.isExpectedField()) { // esperado
												for (AttributeInstance at : attInst) {
													if (at.getAttribute().getId() == a.getId()) {
														at.setFormattedValue(formatValue
																.format(at.getValue().replace(',', '.')));
														expected.add(at.getFormattedValue());
														values.add(at.getFormattedValue());
													}
												}
											} else if (a.isReachedField()) { // realizado
												for (AttributeInstance at : attInst) {
													if (at.getAttribute().getId() == a.getId()) {
														at.setFormattedValue(formatValue.format(at.getValue()));
														reached.add(at.getFormattedValue());
														values.add(at.getFormattedValue());
													}
												}
											}
										}

										meta.put(levelInstanceAux.getSons().getList().get(goalIndex).getId(),
												values);
									}

									List<Long> keys = new ArrayList<Long>(meta.keySet());
									Collections.sort(keys);
									// LOGGER.info(keys);
									int i = 0;
									for (Long x : keys) {
										// LOGGER.info(meta.get(x));
										cell = new PdfPCell(
												new Phrase(levelInstances.getList().get(i).getName(), texto));
										i++;
										cell.setHorizontalAlignment(Element.ALIGN_CENTER);
										cell.setPadding(padding);
										cell.setPaddingTop(capHeight - fontSize + padding);
										cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
										cell.setBackgroundColor(objetivoRowBgColor);
										cell.setBorderColor(borderPlanColor);
										cell.setColspan(2);
										table.addCell(cell);

										if (meta.get(x).size() > 1) {
											cell = new PdfPCell(new Phrase(meta.get(x).get(1), texto));
											cell.setHorizontalAlignment(Element.ALIGN_CENTER);
											cell.setPadding(padding);
											cell.setPaddingTop(capHeight - fontSize + padding);
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(objetivoRowBgColor);
											cell.setBorderColor(borderPlanColor);
											table.addCell(cell);

											cell = new PdfPCell(new Phrase(meta.get(x).get(0), texto));
											cell.setHorizontalAlignment(Element.ALIGN_CENTER);
											cell.setPadding(padding);
											cell.setPaddingTop(capHeight - fontSize + padding);
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(objetivoRowBgColor);
											cell.setBorderColor(borderPlanColor);
											table.addCell(cell);
										} else if (meta.get(x).size() == 0) {
											cell = new PdfPCell(new Phrase("-", texto));
											cell.setHorizontalAlignment(Element.ALIGN_CENTER);
											cell.setPadding(padding);
											cell.setPaddingTop(capHeight - fontSize + padding);
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(objetivoRowBgColor);
											cell.setBorderColor(borderPlanColor);
											table.addCell(cell);

											cell = new PdfPCell(new Phrase("-", texto));
											cell.setHorizontalAlignment(Element.ALIGN_CENTER);
											cell.setPadding(padding);
											cell.setPaddingTop(capHeight - fontSize + padding);
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(objetivoRowBgColor);
											cell.setBorderColor(borderPlanColor);
											table.addCell(cell);

										} else {
											cell = new PdfPCell(new Phrase(meta.get(x).get(0), texto));
											cell.setHorizontalAlignment(Element.ALIGN_CENTER);
											cell.setPadding(padding);
											cell.setPaddingTop(capHeight - fontSize + padding);
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(objetivoRowBgColor);
											cell.setBorderColor(borderPlanColor);
											table.addCell(cell);

											cell = new PdfPCell(new Phrase("-", texto));
											cell.setHorizontalAlignment(Element.ALIGN_CENTER);
											cell.setPadding(padding);
											cell.setPaddingTop(capHeight - fontSize + padding);
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(objetivoRowBgColor);
											cell.setBorderColor(borderPlanColor);
											table.addCell(cell);
										}
									}
								} else {
									cell = new PdfPCell(new Phrase("-", texto));
									cell.setHorizontalAlignment(Element.ALIGN_CENTER);
									cell.setPadding(padding);
									cell.setPaddingTop(capHeight - fontSize + padding);
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setBackgroundColor(objetivoRowBgColor);
									cell.setBorderColor(borderPlanColor);
									cell.setColspan(2);
									table.addCell(cell);
									cell.setColspan(1);
									table.addCell(cell);
									table.addCell(cell);

								}
							}
						}
					}
					tableList.add(table);
				}
			}
		}
	}
	return tableList;
}

/**
 * Gera tabela PDF de atributo do tipo "TableField".
 * 
 * @param tabStructList
 *            Lista de estruturas da tabela (Cabeçalhos).
 * @param tabInstList
 *            Lista de instâncias de tabela (Valores).
 * @return PdfPTable Tabela do atributo em PDF.
 * @throws DocumentException
 */
public PdfPTable returnPdfPTable(List<TableStructure> tabStructList, List<TableInstance> tabInstList,
		boolean hideHeaders, boolean hideBorders) throws DocumentException {
	PdfPTable table = new PdfPTable(tabStructList.size());
	table.setHorizontalAlignment(Element.ALIGN_CENTER);

	if (hideBorders) {
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
	}
	table.setWidthPercentage(100);
	// int[] sizes = new int[tabStructList.size()];
	// int i = 0;

	// ajuste de widths
	/*
	 * for (TableStructure ts : tabStructList) { if (ts.getLabel().length()
	 * < 4) { sizes[i] = 6; } else { String[] split = ts.getLabel().split(
	 * " "); String maior = split[0]; for (int j = 0; j < split.length; j++)
	 * { if (split[j].length() > maior.length()) { maior = split[j]; } } if
	 * (split.length <= 3 && maior.length() < 6) { sizes[i] =
	 * ts.getLabel().length(); } else { sizes[i] = maior.length(); } } i++;
	 * }
	 */
	// table.setWidths(sizes);
	Font textoTabela = FontFactory.getFont(FontFactory.TIMES, 10.0f);
	if (!hideHeaders) {
		for (TableStructure ts : tabStructList) {
			PdfPCell c = new PdfPCell(new Paragraph(ts.getLabel(), textoTabela));
			CMYKColor bgColor = new CMYKColor(55, 45, 42, 7);
			c.setBackgroundColor(bgColor);
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
		}
		for (TableInstance ti : tabInstList) {
			List<TableValues> tabValuesList = fieldsBS.listTableValuesByInstance(ti);
			for (TableValues tv : tabValuesList) {
				if (tv.getTableStructure().getType().equals(Currency.class.getCanonicalName())) {
					table.addCell(new Paragraph(FormatValue.MONETARY.format(tv.getValue()), textoTabela));
				} else if (tv.getTableStructure().getType().equals(Percentage.class.getCanonicalName())) {
					table.addCell(new Paragraph(FormatValue.PERCENTAGE.format(tv.getValue()), textoTabela));
				} else if (tv.getTableStructure().getType().equals(NumberField.class.getCanonicalName())) {
					double integerTest = Double.valueOf(tv.getValue());
					if (integerTest == (int) integerTest) {
						table.addCell(new Paragraph(tv.getValue(), textoTabela));
					} else {
						table.addCell(new Paragraph(FormatValue.NUMERIC.format(tv.getValue()), textoTabela));
					}
				} else if (tv.getTableStructure().getType().equals(ResponsibleField.class.getCanonicalName())) {
					table.addCell(new Paragraph(this.userBS.existsByUser(Long.valueOf(tv.getValue())).getName(),
							textoTabela));
				} else {
					table.addCell(new Paragraph(tv.getValue(), textoTabela));
				}
			}
		}
	} else {
		if (tabStructList.size() == 2) {
			table.setWidths(new float[] { 1, 1 });
		}
		for (TableInstance ti : tabInstList) {
			List<TableValues> tabValuesList = fieldsBS.listTableValuesByInstance(ti);
			for (TableValues tv : tabValuesList) {
				PdfPCell c = new PdfPCell();
				c.setBorder(Rectangle.NO_BORDER);
				// c.setHorizontalAlignment(Element.ALIGN_CENTER);
				Paragraph cellContent = new Paragraph();
				if (tv.getTableStructure().getType().equals(Currency.class.getCanonicalName())) {
					cellContent = new Paragraph(FormatValue.MONETARY.format(tv.getValue()), textoTabela);

				} else if (tv.getTableStructure().getType().equals(Percentage.class.getCanonicalName())) {
					cellContent = new Paragraph(FormatValue.PERCENTAGE.format(tv.getValue()), textoTabela);
				} else if (tv.getTableStructure().getType().equals(ResponsibleField.class.getCanonicalName())) {
					cellContent = new Paragraph(this.userBS.existsByUser(Long.valueOf(tv.getValue())).getName(),
							textoTabela);
				} else {
					cellContent = new Paragraph(tv.getValue(), textoTabela);
				}
				// cellContent.setAlignment(Element.ALIGN_CENTER);
				c.addElement(cellContent);
				table.addCell(c);
			}
		}
	}
	return table;
}

/**
 * Exportar para pdf atributos de um level
 * 
 * @param levelId
 *            Id do level
 * @return
 * @throws MalformedURLException
 * @throws IOException
 * @throws DocumentException
 */
public InputStream exportLevelAttributes(Long levelId)
		throws MalformedURLException, IOException, DocumentException {

	com.itextpdf.text.Document document = new com.itextpdf.text.Document();

	ClassLoader classLoader = getClass().getClassLoader();
	String resourcesPath = new File(classLoader.getResource("/reports/pdf/example.pdf").getFile()).getPath();
	resourcesPath = "/tmp"; // corrigir para salvar com um caminho
	// dinamico
	resourcesPath = resourcesPath.replace("example.pdf", "");
	resourcesPath = resourcesPath.replace("%20", " ");
	File pdfFile = File.createTempFile("output.", ".pdf", new File(resourcesPath));
	InputStream in = new FileInputStream(pdfFile);
	@SuppressWarnings("unused")
	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

	// DEFINIÇÕES DE FONTE, MARGENS, ESPAÇAMENTO E CORES
	Font texto = FontFactory.getFont(FontFactory.TIMES, 12.0f);
	Font textoItalico = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12.0f);

	Font titulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 12.0f);
	// Font tituloCapa = FontFactory.getFont(FontFactory.TIMES_BOLD, 14.0f);
	// Cor cinza - cabeçalho das tabelas
	// CMYKColor headerBgColor = new CMYKColor(55, 45, 42, 7);

	// 0,8 cm acima e abaixo
	float paragraphSpacing = 22.6772f;
	// Parágrafo com 1,25 cm na primeira linha
	// float firstLineIndent = 35.43307f;
	// 1,5 entrelinhas
	// float interLineSpacing = texto.getCalculatedLeading(1.5f);
	// Formato A4 do documento
	document.setPageSize(PageSize.A4);
	// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
	document.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);

	document.open();

	// CABEÇALHO
	String companyLogoUrl = domain.getCompany().getLogo();
	String fpdiLogoUrl = "http://cloud.progolden.com.br/file/8345";// new
																	// File(classLoader.getResource("logo.png").getFile()).getPath();
	if (!companyLogoUrl.trim().isEmpty()) {
		Image companyLogo = Image.getInstance(new URL(companyLogoUrl));
		Image fpdiLogo = Image.getInstance(fpdiLogoUrl);
		// image.scaleAbsolute(150f, 150f);
		float companyLogoScaler = ((document.getPageSize().getWidth() - document.leftMargin()
				- document.rightMargin()) / companyLogo.getWidth()) * 100;
		float fpdiLogoScaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin())
				/ fpdiLogo.getWidth()) * 100;
		companyLogo.scalePercent(companyLogoScaler * 0.25f);
		companyLogo.setAlignment(Element.ALIGN_LEFT);
		fpdiLogo.scalePercent(fpdiLogoScaler * 0.15f);
		fpdiLogo.setAlignment(Element.ALIGN_RIGHT);
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new int[] { 1, 1 });
		table.setSpacingAfter(paragraphSpacing);

		PdfPCell companyCell = new PdfPCell(companyLogo);
		PdfPCell fpdiCell = new PdfPCell(fpdiLogo);

		companyCell.setBorder(Rectangle.NO_BORDER);
		fpdiCell.setBorder(Rectangle.NO_BORDER);
		companyCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		fpdiCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

		table.addCell(companyCell);
		table.addCell(fpdiCell);
		document.add(table);
	}

	StructureLevelInstance levelInstance = this.structHelper.retrieveLevelInstance(levelId);
	String levelInstanceName = levelInstance.getName();
	String levelInstanceType;
	if (levelInstance.getLevel().isIndicator()) {
		if (levelInstance.isAggregate()) {
			levelInstanceType = levelInstance.getLevel().getName() + " agregado";
		} else {
			levelInstanceType = levelInstance.getLevel().getName() + " simples";
		}
	} else {
		levelInstanceType = levelInstance.getLevel().getName();
	}
	String planName = levelInstance.getPlan().getName();
	String planMacroName = levelInstance.getPlan().getParent().getName();

	// PLANO MACRO
	Paragraph planMacroParagraph = new Paragraph(planMacroName, titulo);
	document.add(planMacroParagraph);

	// PLANO DE METAS
	Paragraph planParagraph = new Paragraph(planName, titulo);
	document.add(planParagraph);

	// DATA EXPORTAÇÃO
	SimpleDateFormat brDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	Calendar cal = Calendar.getInstance();
	// Paragraph dataExportacaoLabel = new Paragraph("Data da exportação: ",
	// titulo);
	Paragraph dataExportacaoValue = new Paragraph(brDateFormat.format(cal.getTime()), texto);
	dataExportacaoValue.setSpacingAfter(paragraphSpacing);
	document.add(dataExportacaoValue);

	// NOME DO NIVEL
	Paragraph levelInstanceNameParagraph = new Paragraph(levelInstanceName, titulo);
	document.add(levelInstanceNameParagraph);

	// TIPO DO NIVEL
	Paragraph levelInstanceTypeParagraph = new Paragraph(levelInstanceType, textoItalico);
	document.add(levelInstanceTypeParagraph);

	// RENDIMENTO DO NIVEL
	DecimalFormat decimalFormatDbl = new DecimalFormat("#,##0.00");
	Paragraph proceedsParagraph = new Paragraph();
	Phrase proceedsValue;
	Phrase proceedsLabel = new Phrase("Rendimento atual do nível: ", titulo);
	if (levelInstance.getLevelValue() == null) {
		proceedsValue = new Phrase("0,00%", texto);
	} else {

		proceedsValue = new Phrase(decimalFormatDbl.format(Double.valueOf(levelInstance.getLevelValue())) + "%",
				texto);
	}
	proceedsParagraph.add(proceedsLabel);
	proceedsParagraph.add(proceedsValue);
	proceedsParagraph.setSpacingAfter(paragraphSpacing);
	document.add(proceedsParagraph);

	List<Attribute> attrList = this.structureBS.retrieveLevelSonsAttributes(levelInstance);

	for (Attribute attribute : attrList) {
		// LOGGER.info(attribute.toString());
		if (attribute.isRequired() || attribute.getType().equals(BudgetField.class.getCanonicalName())
				|| attribute.isReachedField()) {
			// LOGGER.info(attribute.toString());

			Paragraph attributeParagraph = new Paragraph();
			Phrase attributeValue = new Phrase();
			Phrase attributeLabel = new Phrase(attribute.getLabel() + ": ", titulo);

			if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
				if (attribute.getAttributeInstances().get(0) != null) {

					User responsible = this.userBS
							.existsByUser(Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
					if (responsible != null) {
						attributeValue = new Phrase(responsible.getName(), texto);
						attributeParagraph.add(attributeLabel);
						attributeParagraph.add(attributeValue);
						document.add(attributeParagraph);
					}
				}
			} else if (attribute.getType().equals(ActionPlanField.class.getCanonicalName())) {

				PaginatedList<ActionPlan> actionPlanList = this.fieldsBS.listActionPlansByInstance(levelInstance);
				if (actionPlanList.getList() != null && !actionPlanList.getList().isEmpty()) {
					attributeParagraph.setSpacingBefore(paragraphSpacing);
					attributeLabel = new Phrase(attribute.getLabel(), titulo);
					attributeParagraph.add(attributeLabel);
					document.add(attributeParagraph);

					PdfPTable table = new PdfPTable(4);
					table.setSpacingBefore(paragraphSpacing / 2);
					table.setWidthPercentage(100);
					table.getDefaultCell();
					PdfPCell c = new PdfPCell(new Paragraph("Ação", texto));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					// c.setBackgroundColor(headerBgColor);
					table.addCell(c);
					c = new PdfPCell(new Paragraph("Responsável", texto));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					// c.setBackgroundColor(headerBgColor);
					table.addCell(c);

					c = new PdfPCell(new Paragraph("Início", texto));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					// c.setBackgroundColor(headerBgColor);
					table.addCell(c);

					c = new PdfPCell(new Paragraph("Fim", texto));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					// c.setBackgroundColor(headerBgColor);
					table.addCell(c);

					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					for (ActionPlan acp : actionPlanList.getList()) {
						table.addCell(new Paragraph(acp.getDescription(), texto));
						table.addCell(new Paragraph(acp.getResponsible(), texto));
						table.addCell(new Paragraph(sdf.format(acp.getBegin()), texto));
						table.addCell(new Paragraph(sdf.format(acp.getEnd()), texto));
					}
					document.add(table);
				}
			} else if (attribute.getType().equals(BudgetField.class.getCanonicalName())) {
				List<BudgetDTO> budgetList = new ArrayList<BudgetDTO>();
				budgetList = this.fieldsBS.getBudgets(levelInstance);
				if (!budgetList.isEmpty()) {
					attributeParagraph.setSpacingBefore(paragraphSpacing);
					attributeLabel = new Phrase(attribute.getLabel(), titulo);
					attributeParagraph.add(attributeLabel);
					document.add(attributeParagraph);

					// Orçamento
					PdfPTable table = new PdfPTable(4);
					table.setSpacingBefore(paragraphSpacing / 2);
					table.setWidthPercentage(100);

					// Subação
					PdfPCell cell = new PdfPCell(new Phrase("Subação", texto));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					// Planejado
					cell = new PdfPCell(new Phrase("Planejado", texto));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					// Empenhado
					cell = new PdfPCell(new Phrase("Empenhado", texto));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					// Realizado
					cell = new PdfPCell(new Phrase("Realizado", texto));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					for (BudgetDTO b : budgetList) {
						// Subação - valor
						cell = new PdfPCell(new Phrase(b.getBudget().getSubAction(), texto));
						table.addCell(cell);

						// Para formatar valores em R$
						Locale ptBr = new Locale("pt", "BR"); // Locale
																// para
																// o
																// Brasil
						NumberFormat moedaFormat = NumberFormat.getCurrencyInstance(ptBr);

						// Planejado - valor
						cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudgetLoa()), texto));
						table.addCell(cell);

						// Empenhado - valor
						cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudget().getCommitted()), texto));
						table.addCell(cell);

						// Realizado - valor
						cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudget().getRealized()), texto));
						table.addCell(cell);
					}
					document.add(table);
				}

			} else {
				if (levelInstance.getLevel().isGoal()) {
					AttributeInstance attinst = attribute.getAttributeInstances().get(0);
					if (attribute.isMaximumField() || attribute.isMinimumField() || attribute.isExpectedField()
							|| attribute.isReachedField()) {

						FormatValue formatValue = FormatValue.forAttributeInstance(
								this.attrHelper.retrieveFormatAttributeInstance(levelInstance.getParent()));
						if (attinst != null) {
							if (attinst.getValue() != null)
								attributeValue = new Phrase(formatValue.format(attinst.getValue()), texto);
							else
								attributeValue = new Phrase("-", texto);
						} else {
							attributeValue = new Phrase("-", texto);
						}
					} else {
						if (attinst != null) {
							attributeValue = new Phrase(attinst.getValue(), texto);
						}
					}
					if (attinst != null) {
						attributeParagraph.add(attributeLabel);
						attributeParagraph.add(attributeValue);
						document.add(attributeParagraph);
					}
				} else {
					if (attribute.getAttributeInstances().get(0) != null) {
						attributeValue = new Phrase(attribute.getAttributeInstances().get(0).getValue(), texto);
						attributeParagraph.add(attributeLabel);
						attributeParagraph.add(attributeValue);
						document.add(attributeParagraph);
					}
				}
			}

		}
	}

	if (levelInstance.isAggregate()) {
		
		List<AggregateIndicator> levelList=	this.docBS.listAggIndbyLevelInstance(levelInstance);


		if (!levelList.isEmpty()) {
			Paragraph aggParagraphLabel = new Paragraph();
			aggParagraphLabel.setSpacingBefore(paragraphSpacing);
			aggParagraphLabel.add(new Phrase("Indicadores", titulo));
			document.add(aggParagraphLabel);

			PdfPTable table = new PdfPTable(5);
			table.setSpacingBefore(paragraphSpacing / 2);
			table.setWidthPercentage(100);

			PdfPCell cell = new PdfPCell(new Phrase("Nome", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Responsável", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Início", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Fim", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Rendimento", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			for (AggregateIndicator son : levelList) {

				cell = new PdfPCell(new Phrase(son.getAggregate().getName(), texto));
				table.addCell(cell);

				List<Attribute> sonAttrList = this.structureBS.retrieveLevelSonsAttributes(son.getAggregate());
				User responsible = new User();
				Date beginDate = new Date();
				Date endDate = new Date();
				for (Attribute attribute : sonAttrList) {
					if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
						responsible = this.userBS
								.existsByUser(Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
					} else if (attribute.isBeginField()) {
						beginDate = attribute.getAttributeInstances().get(0).getValueAsDate();
					} else if (attribute.isEndField()) {
						endDate = attribute.getAttributeInstances().get(0).getValueAsDate();
					}
				}

				if (responsible != null)
					cell = new PdfPCell(new Phrase(responsible.getName(), texto));
				else
					cell = new PdfPCell(new Phrase("-", texto));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(brDateFormat.format(beginDate), texto));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(brDateFormat.format(endDate), texto));
				table.addCell(cell);

				if (son.getAggregate().getLevelValue() != null)
					cell = new PdfPCell(new Phrase(
							decimalFormatDbl.format(Double.valueOf(son.getAggregate().getLevelValue())) + "%",
							texto));
				else
					cell = new PdfPCell(new Phrase("0,00%", texto));
				table.addCell(cell);
			}
			document.add(table);

		}

	}

	PaginatedList<StructureLevelInstance> sonsList = new PaginatedList<>();
	sonsList.setList(structureBS.retrieveLevelInstanceSons(levelInstance.getId()));
	levelInstance.setSons(sonsList);
	sonsList = levelInstance.getSons();
	Paragraph sonParagraphLabel = new Paragraph();
	// LOGGER.info(sonsList.getList().toString());
	if (!sonsList.getList().isEmpty()) {
		if (sonsList.getList().get(0).getLevel().isObjective()) {
			sonParagraphLabel.setSpacingBefore(paragraphSpacing);
			sonParagraphLabel.add(new Phrase("Objetivos", titulo));
			document.add(sonParagraphLabel);

			PdfPTable table = new PdfPTable(3);
			table.setSpacingBefore(paragraphSpacing / 2);
			table.setWidthPercentage(100);

			PdfPCell cell = new PdfPCell(new Phrase("Nome", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Responsável", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Rendimento", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			for (StructureLevelInstance son : sonsList.getList()) {
				cell = new PdfPCell(new Phrase(son.getName(), texto));
				table.addCell(cell);

				List<Attribute> sonAttrList = this.structureBS.retrieveLevelSonsAttributes(son);
				User responsible = new User();
				for (Attribute attribute : sonAttrList) {
					if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
						if (attribute.getAttributeInstances().get(0) != null) {
							responsible = this.userBS.existsByUser(
									Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
						} else {
							responsible = null;
						}
						break;
					}
				}
				if (responsible != null)
					cell = new PdfPCell(new Phrase(responsible.getName(), texto));
				else
					cell = new PdfPCell(new Phrase("-", texto));

				table.addCell(cell);

				if (son.getLevelValue() != null)
					cell = new PdfPCell(
							new Phrase(decimalFormatDbl.format(Double.valueOf(son.getLevelValue())) + "%", texto));
				else
					cell = new PdfPCell(new Phrase("0,00%", texto));
				table.addCell(cell);
			}
			document.add(table);
		} else if (sonsList.getList().get(0).getLevel().isIndicator()) {
			sonParagraphLabel.setSpacingBefore(paragraphSpacing);
			sonParagraphLabel.add(new Phrase("Indicadores", titulo));
			document.add(sonParagraphLabel);

			PdfPTable table = new PdfPTable(5);
			table.setSpacingBefore(paragraphSpacing / 2);
			table.setWidthPercentage(100);

			PdfPCell cell = new PdfPCell(new Phrase("Nome", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Responsável", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Início", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Fim", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Rendimento", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			for (StructureLevelInstance son : sonsList.getList()) {
				cell = new PdfPCell(new Phrase(son.getName(), texto));
				table.addCell(cell);

				List<Attribute> sonAttrList = this.structureBS.retrieveLevelSonsAttributes(son);
				User responsible = new User();
				Date beginDate = new Date();
				Date endDate = new Date();
				for (Attribute attribute : sonAttrList) {
					if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
						if (attribute.getAttributeInstances().get(0) != null) {
							responsible = this.userBS.existsByUser(
									Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
						} else {
							responsible = null;
						}
					} else if (attribute.isBeginField()) {
						if (attribute.getAttributeInstances().get(0) != null)
							beginDate = attribute.getAttributeInstances().get(0).getValueAsDate();
						else
							beginDate = null;
					} else if (attribute.isEndField()) {
						if (attribute.getAttributeInstances().get(0) != null)
							endDate = attribute.getAttributeInstances().get(0).getValueAsDate();
						else
							endDate = null;
					}
				}

				if (responsible != null)
					cell = new PdfPCell(new Phrase(responsible.getName(), texto));
				else
					cell = new PdfPCell(new Phrase("-", texto));
				table.addCell(cell);
				if (beginDate != null) {
					cell = new PdfPCell(new Phrase(brDateFormat.format(beginDate), texto));
				} else {
					cell = new PdfPCell(new Phrase("-", texto));
				}
				table.addCell(cell);

				if (endDate != null) {
					cell = new PdfPCell(new Phrase(brDateFormat.format(endDate), texto));
				} else {
					cell = new PdfPCell(new Phrase("-", texto));
				}
				table.addCell(cell);

				if (son.getLevelValue() != null)
					cell = new PdfPCell(
							new Phrase(decimalFormatDbl.format(Double.valueOf(son.getLevelValue())) + "%", texto));
				else
					cell = new PdfPCell(new Phrase("0,00%", texto));
				table.addCell(cell);

			}
			document.add(table);
		} else if (sonsList.getList().get(0).getLevel().isGoal()) {
			sonParagraphLabel.setSpacingBefore(paragraphSpacing);
			sonParagraphLabel.add(new Phrase("Metas", titulo));
			document.add(sonParagraphLabel);

			PdfPTable table = new PdfPTable(3);
			table.setSpacingBefore(paragraphSpacing / 2);
			table.setWidthPercentage(100);

			PdfPCell cell = new PdfPCell(new Phrase("Nome", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Responsável", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Desempenho", texto));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			for (StructureLevelInstance son : sonsList.getList()) {
				cell = new PdfPCell(new Phrase(son.getName(), texto));
				table.addCell(cell);

				List<Attribute> sonAttrList = this.structureBS.retrieveLevelSonsAttributes(son);
				User responsible = new User();
				for (Attribute attribute : sonAttrList) {
					if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
						if (attribute.getAttributeInstances().get(0) != null) {
							responsible = this.userBS.existsByUser(
									Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
						} else {
							responsible = null;
						}
						break;
					}
				}

				if (responsible != null)
					cell = new PdfPCell(new Phrase(responsible.getName(), texto));
				else
					cell = new PdfPCell(new Phrase("-", texto));

				table.addCell(cell);

				if (son.getLevelValue() != null)
					cell = new PdfPCell(
							new Phrase(decimalFormatDbl.format(Double.valueOf(son.getLevelValue())) + "%", texto));
				else
					cell = new PdfPCell(new Phrase("0,00%", texto));
				table.addCell(cell);
			}
			document.add(table);
		}
	}

	document.close();

	return in;
}

/**
 * Realiza as funções necessárias para gerar o PDF
 * 
 * @param src
 * @param dest
 * @param document
 * @throws IOException
 * @throws DocumentException
 */
public void manipulatePdf(String src, String dest, com.itextpdf.text.Document document, int unnumbered)
		throws IOException, DocumentException {
	PdfReader reader = new PdfReader(src);
	int n = reader.getNumberOfPages();
	PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
	PdfContentByte pagecontent;

	Font texto = FontFactory.getFont(FontFactory.TIMES, 10.0f);
	for (int i = 0; i < n;) {
		pagecontent = stamper.getOverContent(++i);
		if (i > unnumbered)
			ColumnText.showTextAligned(pagecontent, Element.ALIGN_RIGHT, new Phrase(String.format("%s", i), texto),
					// new Phrase(String.format("Página %s de %s", i, n),
					// texto),
					document.right(), document.bottom(), 0);
	}
	stamper.close();
	reader.close();
}



	class TOCEvent extends PdfPageEventHelper {
	
		protected int counter = 0;
		protected List<SimpleEntry<String, SimpleEntry<String, Integer>>> toc = new ArrayList<>();
	
		String lastText = "";
	
		@Override
		public void onGenericTag(PdfWriter writer, com.itextpdf.text.Document document, Rectangle rect,
				String text) {
			if (text != lastText) {
				String name = "dest" + (counter++);
				int page = writer.getPageNumber();
				toc.add(new SimpleEntry<String, SimpleEntry<String, Integer>>(text,
						new SimpleEntry<String, Integer>(name, page)));
			}
			lastText = text;
		}
		public List<SimpleEntry<String, SimpleEntry<String, Integer>>> getTOC() {
			return toc;
		}
	}

	// DEFINIÇÕES DE FONTE, MARGENS, ESPAÇAMENTO E CORES
	Font texto = FontFactory.getFont(FontFactory.TIMES, 12.0f);

	Font titulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 12.0f);
	Font tituloCapa = FontFactory.getFont(FontFactory.TIMES_BOLD, 14.0f);
	// Cor cinza - cabeçalho das tabelas
	CMYKColor headerBgColor = new CMYKColor(55, 45, 42, 7);

	// 0,8 cm acima e abaixo
	float paragraphSpacing = 22.6772f;
	// Parágrafo com 1,25 cm na primeira linha
	float firstLineIndent = 35.43307f;
	// 1,5 entrelinhas
	float interLineSpacing = texto.getCalculatedLeading(1.5f);
	// Formato A4 do documento
	
	private void generateCover(File coverPdfFile, String title, String author) throws DocumentException, IOException, MalformedURLException {
		
		com.itextpdf.text.Document coverDocument = new com.itextpdf.text.Document();
		PdfWriter coverWriter = PdfWriter.getInstance(coverDocument, new FileOutputStream(coverPdfFile));

		
		coverDocument.setPageSize(PageSize.A4);
		// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
		coverDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);

		coverDocument.open();

		// CABEÇALHO
		String imageUrl = domain.getCompany().getLogo();
		// LOGGER.info("|"+imageUrl+"|");
		if (!imageUrl.trim().isEmpty()) {
			Image image = Image.getInstance(new URL(imageUrl));
			// image.scaleAbsolute(150f, 150f);
			float scaler = ((coverDocument.getPageSize().getWidth() - coverDocument.leftMargin()
					- coverDocument.rightMargin()) / image.getWidth()) * 100;
			image.scalePercent(scaler * 0.4f);
			image.setAlignment(Element.ALIGN_CENTER);
			coverDocument.add(image);
		}

		Paragraph TITULO = new Paragraph(title, tituloCapa);
		Paragraph AUTHOR = new Paragraph(author, texto);
		TITULO.setAlignment(Element.ALIGN_CENTER);
		TITULO.setSpacingBefore(paragraphSpacing * 8);

		 AUTHOR.setAlignment(Element.ALIGN_CENTER);

		coverDocument.add(TITULO);
		// document.add(AUTHOR);
		// document.add(YEAR);
		Phrase localizationPhrase = new Phrase(this.domain.getCompany().getLocalization(), titulo);
		// Phrase footer = new Phrase(String.valueOf(cal.get(Calendar.YEAR)),
		// titulo);
		PdfContentByte cb = coverWriter.getDirectContent();
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, localizationPhrase,
				(coverDocument.right() - coverDocument.left()) / 2 + coverDocument.leftMargin(),
				coverDocument.bottom() + 30, 0);
		coverDocument.newPage();

		// FOLHA DE ROSTO
		Paragraph COMPANY = new Paragraph(domain.getCompany().getName(), tituloCapa);
		COMPANY.setAlignment(Element.ALIGN_CENTER);
		COMPANY.setSpacingBefore(paragraphSpacing);
		coverDocument.add(COMPANY);

		coverDocument.add(AUTHOR);
		//coverDocument.add(TITULO);
		
		Calendar cal = Calendar.getInstance();

		Phrase periodPhrase = new Phrase(
				String.valueOf(cal.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cal.get(Calendar.YEAR)), titulo);
		// footerRosto.add(periodPhrase);
		// footerRosto.add(footer);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, periodPhrase,
				(coverDocument.right() - coverDocument.left()) / 2 + coverDocument.leftMargin(),
				coverDocument.bottom() + 15, 0);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, localizationPhrase,
				(coverDocument.right() - coverDocument.left()) / 2 + coverDocument.leftMargin(),
				coverDocument.bottom() + 30, 0);
		coverDocument.newPage();
		coverDocument.close();
	
		//return coverWriter;
	}
	
	/*política*/
	
	private String getMatrixValue(Policy policy, String[][] matrix, int line, int column) {

		String result="";

		for(int i=0; i<matrix.length;i++){
			if(Integer.parseInt(matrix[i][1])==line){
				if(Integer.parseInt(matrix[i][2])==column){
					if(Integer.parseInt(matrix[i][2])==0){
						return "<div style=\"text-align:right;\">"+matrix[i][0]+"&nbsp;&nbsp;&nbsp;&nbsp;</div>";
					}else if(Integer.parseInt(matrix[i][1])==policy.getNline()){
						return "<div style=\"text-align:-webkit-center;margin: 5px\">"+matrix[i][0]+"</div>";
					}else{

						int current_color=-1;
						String cor="";
						PaginatedList<RiskLevel> risklevel = this.policyBS.listRiskLevelbyPolicy(policy);
						if(risklevel != null){
							for(int k=0; k< risklevel.getTotal();k++){
								if(risklevel.getList().get(k).getLevel().equals(matrix[i][0])){
									current_color=risklevel.getList().get(k).getColor();
									break;
								}
							}
						}

						switch(current_color) {
							case 0: cor="Vermelho"; break;
							case 1: cor="Marron"; break;
							case 2: cor="Amarelo"; break;
							case 3: cor="Laranja"; break;
							case 4: cor="Verde"; break;
							case 5: cor="Azul"; break;
							default: cor="Cinza";
						}

					return "<div class=\"Cor "+cor+"\">"+matrix[i][0]+"</div>";

					}
				}
			}
		}
		return "";
	}
	
	private BaseColor getMatrixWebColor(Policy policy, String[][] matrix, int line, int column) {

		String result="";

		for(int i=0; i<matrix.length;i++){
			if(Integer.parseInt(matrix[i][1])==line){
				if(Integer.parseInt(matrix[i][2])==column){
					if(Integer.parseInt(matrix[i][2])==0){
						return null;
					}else if(Integer.parseInt(matrix[i][1])==policy.getNline()){
						return null;
					}else{

						PaginatedList<RiskLevel> risklevel = this.policyBS.listRiskLevelbyPolicy(policy);
						int current_color=-1;
						if(risklevel != null){
							for(int k=0; k< risklevel.getTotal();k++){
								if(risklevel.getList().get(k).getLevel().equals(matrix[i][0])){
									current_color=risklevel.getList().get(k).getColor();
									break;
								}
							}
						}

						switch(current_color) {
							case 0: return WebColors.getRGBColor("#FF0000");
							case 1: return WebColors.getRGBColor("#774422");
							case 2: return WebColors.getRGBColor("#FFFF00");
							case 3: return WebColors.getRGBColor("#FF6600");
							case 4: return WebColors.getRGBColor("#00CC00");
							case 5: return WebColors.getRGBColor("#00FF00");
							default: return WebColors.getRGBColor("#ddd");
						}


					}
				}
			}
		}
		return null;
	}
	
	private void generateContent(File contentFile, String itens, String subitens, TOCEvent event) throws DocumentException, IOException, MalformedURLException {
			
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(contentFile));
		
		writer.setPageEvent(event);
		
		File outputDir;

		outputDir = contentFile.getParentFile();

		final String prefix = String.format("frisco-report-export-%d", System.currentTimeMillis());
		
		
		String[] sections = null;
		if (itens != null)
			sections = itens.split(",");
		
		String[] subsections= null;
		if (subitens != null)
			subsections = subitens.split(",");
		
		int secIndex = 0;
		int subSecIndex = 0;
		boolean lastAttWasPlan = false;
		boolean haveContent = false;

		document.open();
		document.add(new Chunk(""));
		
		//para cada item selecionado
		if(sections !=null) {
			for (int i = 0; i < sections.length; i++) {
				Item item = this.itemBS.retrieveItembyId(Long.parseLong(sections[i]));//item altual
				PaginatedList<FieldItem> fielditens = this.itemBS.listFieldsByItem(item);//fields atual
				PaginatedList<SubItem> subs = this.itemBS.listSubItensByItem(item);	//lista todos subitens
				List <SubItem> actualsubitens= new ArrayList<SubItem>();	//lista de subitens selecionados
	
				//lista subitens selecionados
				for(SubItem sub : subs.getList()) {
					if(subsections !=null) {
						for (int j = 0; j < subsections.length; j++) {
							if(sub.getId() == Long.parseLong(subsections[j])) {
								actualsubitens.add(sub);
							}
						}
					}
				}
					
				//haveContent = true;
				boolean secTitlePrinted = false;
				subSecIndex = 0;
				String secName =item.getName();
	
				if(fielditens.getTotal()>0){
					secIndex++;
				}
				
				//informações gerais matrix
				if(item.getDescription()!=null) {
					haveContent = true;
					secIndex++;
					Chunk c = new Chunk(secIndex + ". " + secName, titulo);
					c.setGenericTag(secIndex + ". " + secName);
					Paragraph secTitle = new Paragraph(c);
					secTitle.setLeading(interLineSpacing);
					secTitle.setSpacingAfter(paragraphSpacing);
					//secTitle.setSpacingBefore(paragraphSpacing);
					document.add(secTitle);
					
					
					Paragraph attTitle = new Paragraph("Descrição", titulo);
					attTitle.setLeading(interLineSpacing);
					attTitle.setSpacingAfter(paragraphSpacing);
					attTitle.setSpacingBefore(paragraphSpacing);
					document.add(attTitle);
	
					Paragraph description = new Paragraph(item.getDescription());
					description.setIndentationLeft(firstLineIndent);
					description.setSpacingAfter(paragraphSpacing);
					document.add(description);
					
					Policy policy = this.policyBS.exists(item.getPolicy().getId(), Policy.class);
					
					
					//matrix de risco
					
					String css=".Cor {\n" + 
					//"  padding: 30px 25px;\n" + 
							"margin: 10px\n "+
					"  white-space: nowrap;\n" + 
					"  border: .5px solid #ddd;\n" + 
					"  text-align: -webkit-center; }\n" + 
					"\n" + 
					".Cinza {\n" + 
					"  background-color: #ddd; }\n" + 
					"\n" + 
					".Vermelho {\n" + 
					"  background-color: #FF0000;\n" + 
					"  color: white; }\n" + 
					"\n" + 
					".Marron {\n" + 
					"  background-color: #774422;\n" + 
					"  color: white; }\n" + 
					"\n" + 
					".Amarelo {\n" + 
					"  background-color: #FFFF00; }\n" + 
					"\n" + 
					".Laranja {\n" + 
					"  background-color: #FF6600;\n" + 
					"  color: white; }\n" + 
					"\n" + 
					".Verde {\n" + 
					"  background-color: #00CC00;\n" + 
					"  color: white; }\n" + 
					"\n" + 
					".Azul {\n" + 
					"  background-color: #0000FF;\n" + 
					"  color: white; }\n" + 
					"  \n" + 
					"  .vertical-text {\n" + 
					"  -webkit-transform: rotate(270deg);\n" + 
					"          transform: rotate(270deg); "
					+ "}\n"+
					".rotate{\n"+
					"	width: 135px;\n"+
					"	bottom: 35px;\n"+
					"	position: relative;\n"+
					"	transform:rotate(270deg);}\n";
					
                    
					String[][] matrix = this.riskBS.getMatrixVector(policy);
					String table = "";
					for (int x=0; x<=policy.getNline();x++){
						String children ="";
						for (int y=0; y<=policy.getNcolumn();y++){
							children+="				"+
									"<td id=\"x-"+x+"-y"+y+"\">"+this.getMatrixValue(policy,matrix,x,y)+"</td>\n";
						}
						table+="			"+
								"<tr id=\"x-"+x+"\">\n"+children+"			"+"</tr>\n";
					}
					
					String html="<html>\n" + 
							"<head>\n" +
							"</head>\n" + 
							"<body>"+
							"<div>\n"+
							"<table style=\"width:100%\">\n" +
							table + 

							
							/*"			<div>\n" + 
							"				<div style=\"bottom: "+((policy.getNline()-2)*20+80)+"px ; right: 50px ; position: relative\">\n" + 
							"					<div class=\"rotate vertical-text \">PROBABILIDADE</div>\n" + 
							"				</div>\n" + 
							"			</div>\n" + 
							
							"			<tr>\n" + 
							"				<th></th>\n" + 
							"				<th colspan=\""+String.valueOf(policy.getNcolumn())+"\" style=\"text-align:-webkit-center;\">\n"+
							"					IMPACTO"+
							"				</th>\n"+	
							"			</tr>\n" +*/
							"</table>"+
							"</div>\n"+
							"</body>\n"+
							"</html>";
					
					Paragraph attTitleInfo = new Paragraph("Matrix De Risco", titulo);
					attTitle.setLeading(interLineSpacing);
					attTitle.setSpacingBefore(paragraphSpacing);
					attTitle.setSpacingAfter(paragraphSpacing);
					document.add(attTitleInfo);
					
					StyleSheet styles = new StyleSheet();

                    
                    

					
					File htmlFile = new File(outputDir, String.format("%s-0.html", prefix));
					FileOutputStream out = new FileOutputStream(htmlFile);
					out.write(html.getBytes());
					out.close();

					FileReader fr = new FileReader(htmlFile.getPath());
					List<Element> p = HTMLWorker.parseToList(fr, styles);
					 
					//InputStream is = new ByteArrayInputStream(str.getBytes());
			        //XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

			        //String html ="<html><h1>Header</h1><p>A paragraph</p><p>Another Paragraph</p></html>";

					
					/*
					*/
			       /* ElementList elementsList = XMLWorkerHelper.parseToElementList(html, css);
			        
			        for(Element element : elementsList) {
			        	document.add(element);
			        }*/
			        

			        
					fr.close();
	
					for (int k = 0; k < p.size(); ++k) {
						
						if (p.get(k) instanceof PdfPTable) {
							PdfPTable att = (PdfPTable) p.get(k);
							
							ArrayList<PdfPRow> rows = att.getRows();
							
							for(int y = 0; y< rows.size();y++) {
								
								PdfPCell[] cells = rows.get(y).getCells();
								
								for(int x=0; x<cells.length ;x++) {
									if(cells[x] != null) {
										
										BaseColor bc = this.getMatrixWebColor(policy, matrix, y,x);

										cells[x].setBackgroundColor(bc);
										cells[x].setBorder(2);
										//cells[x].setHorizontalAlignment(Element.ALIGN_CENTER);
										//cells[x].setVerticalAlignment(Element.ALIGN_MIDDLE);
										
										cells[x].setBorderColor( WebColors.getRGBColor("#ddd"));
										//cells[x].setExtraParagraphSpace(2);
										if(bc != null) {
											cells[x].setBorderWidth((float) 1.0);
											cells[x].setBorderWidthRight((float) 1.0);
											cells[x].setPaddingBottom(15);
											cells[x].setPaddingTop(15);
											cells[x].setPaddingLeft(10);
											cells[x].setPaddingRight(10);
										}else {
											//if(x==policy.getNline()*policy.getNcolumn()) {
											if(x==0 && y==policy.getNline()+1) {
												cells[x].setRotation(90);
												//cells[x].setBottom(100);
												//cells[x].setLeft(20);
											}
											
											if(y==policy.getNline()+1) {
												cells[x].setBottom((float) 100);
											}
										}
										
									}
								}
							}
							document.add(att);
						}
					}
					
					for (int k = 0; k < p.size(); ++k) {
						
						if (p.get(k) instanceof Paragraph) {
							Paragraph att = (Paragraph) p.get(k);
							document.add(att);
						}
						
					}
					
				}


				
				
				
				for (FieldItem fielditem: fielditens.getList()) {
					
					haveContent = true;
					
					if( fielditem.isText() && fielditem.getDescription() != null && !fielditem.getDescription().equals("")) {
						
						if (lastAttWasPlan) {
							document.setPageSize(PageSize.A4);
							document.newPage();
						}
						if (!secTitlePrinted) {
							Chunk c = new Chunk(secIndex + ". " + secName, titulo);
							c.setGenericTag(secIndex + ". " + secName);
							Paragraph secTitle = new Paragraph(c);
							secTitle.setLeading(interLineSpacing);
							secTitle.setSpacingAfter(paragraphSpacing);
							secTitle.setSpacingBefore(paragraphSpacing);
							document.add(secTitle);
							secTitlePrinted = true;
						}
						
						String attName = fielditem.getName();
						
						if (!attName.equals(secName)) {
							Paragraph attTitle = new Paragraph(attName, titulo);
							attTitle.setLeading(interLineSpacing);
							attTitle.setSpacingAfter(paragraphSpacing);
							attTitle.setSpacingBefore(paragraphSpacing);
							document.add(attTitle);
						}
						
						Map<String, String> pc2 = new HashMap<String, String>();
						pc2.put("line-height", "115%");
						pc2.put("margin-bottom", "6.0pt");
						pc2.put("text-align", "center");
						HashMap<String, String> spanc1 = new HashMap<String, String>();
						spanc1.put("text-justify", "inter-word");
	
						StyleSheet styles = new StyleSheet();
						styles.loadTagStyle("p", "text-indent", "1.25cm");
	
						String str = "<html>" + "<head>" + "</head><body style=\"text-indent: 1.25cm; \">"
								+ "<p style=\"text-indent: 1.25cm; \">";
							
						Queue<String> allMatches = new LinkedList<>();
						String value = fielditem.getDescription();
						if (fielditem.getDescription().contains("<img")) {
							Matcher m = Pattern.compile("<img [^>]*>").matcher(fielditem.getDescription());
							while (m.find()) {
								String match = m.group();
								allMatches.add(match);
								value = value.replace(match, "<p>||IMAGE||</p>");
							}
						}
						str += value + "</p></body></html>";
						
						File htmlFile = new File(outputDir, String.format("%s-1.html", prefix));
						FileOutputStream out = new FileOutputStream(htmlFile);
						out.write(str.getBytes());
						out.close();
	
						FileReader fr = new FileReader(htmlFile.getPath());
						
						List<Element> p = HTMLWorker.parseToList(fr, styles);
						
						fr.close();
						
						for (int k = 0; k < p.size(); ++k) {
							if (p.get(k) instanceof Paragraph) {
								Paragraph att = (Paragraph) p.get(k);
								// LOGGER.info("------->"+att.getContent());
								if (att.getContent().contains("||IMAGE||")) {
									String img = allMatches.poll();
									if (img != null 
											&& !img.substring(0, 100).contains("data:image/png;base64")
											&& !img.substring(0, 100).contains("data:image/jpg;base64")) {

										// LOGGER.info("IMG------->"+img);
										Image image = Image.getInstance(
												new URL(img.replaceAll("<img src=\"", "").replaceAll("\">", "").split("\"")[0]));
										float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
												- document.rightMargin()) / image.getWidth()) * 100;
										image.scalePercent(scaler * 0.4f);
										image.setAlignment(Element.ALIGN_CENTER);
										document.add(image);
										
									}else if(img != null ){
										try {
											img=img.replace("<img src=\"", "").replace("\">", "");
											final String base64Data = img.substring(img.indexOf(",") + 1);
											Image image = null;
											image = Image.getInstance(Base64.decode(base64Data));
	
											if (image != null) {
												float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
															- document.rightMargin()) / image.getWidth()) * 100;
												image.scalePercent(scaler * 0.4f);
												document.add(image);
											}
										}catch(Exception e) {
											LOGGER.error("Imagem não foi exportada no documento pdf.");
										}
									}
								} else {
									att.setFirstLineIndent(firstLineIndent);
									document.add(att);
								}
							} else if (p.get(k).getClass().getName().equals("com.itextpdf.text.List")) {
								com.itextpdf.text.List att = (com.itextpdf.text.List) p.get(k);
								att.setIndentationLeft(firstLineIndent);
								document.add(att);
							}
						}
						lastAttWasPlan = false;
						htmlFile.delete();
					}
				}
					
				subSecIndex = 0;
				secTitlePrinted=false;
	
				for (SubItem sub: actualsubitens) {
						
					haveContent = true;
					subSecIndex++;
					
					String subSecName =sub.getName();
					
					PaginatedList<FieldSubItem> fieldsubs = this.itemBS.listFieldsBySubItem(sub);
						
					for(FieldSubItem fieldsub : fieldsubs.getList()) {
							
						if( fieldsub.isText() && fieldsub.getDescription() != null && !fieldsub.getDescription().equals("")) {
						
							if (lastAttWasPlan) {
								document.setPageSize(PageSize.A4);
								document.newPage();
							}
							if (!secTitlePrinted) {
								Chunk c = new Chunk(secIndex + "." + subSecIndex + ". " + subSecName, titulo);
								c.setGenericTag(secIndex + "." + subSecIndex + ". " + subSecName);
								Paragraph secTitle = new Paragraph(c);
								secTitle.setLeading(interLineSpacing);
								secTitle.setSpacingAfter(paragraphSpacing);
								secTitle.setSpacingBefore(paragraphSpacing);
								document.add(secTitle);
								//secTitlePrinted = true;
							}
							
							String attName = fieldsub.getName();
							
							if (!attName.equals(secName)) {
								Paragraph attTitle = new Paragraph(attName, titulo);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
							
							Map<String, String> pc2 = new HashMap<String, String>();
							pc2.put("line-height", "115%");
							pc2.put("margin-bottom", "6.0pt");
							pc2.put("text-align", "center");
							HashMap<String, String> spanc1 = new HashMap<String, String>();
							spanc1.put("text-justify", "inter-word");
		
							StyleSheet styles = new StyleSheet();
							styles.loadTagStyle("p", "text-indent", "1.25cm");
		
							String str = "<html>" + "<head>" + "</head><body style=\"text-indent: 1.25cm; \">"
									+ "<p style=\"text-indent: 1.25cm; \">";
								
							Queue<String> allMatches = new LinkedList<>();
							String value = fieldsub.getDescription();
							if (fieldsub.getDescription().contains("<img")) {
								Matcher m = Pattern.compile("<img [^>]*>").matcher(fieldsub.getDescription());
								while (m.find()) {
									String match = m.group();
									allMatches.add(match);
									value = value.replace(match, "<p>||IMAGE||</p>");
								}
							}
							str += value + "</p></body></html>";
							File htmlFile = new File(outputDir, String.format("%s-2.html", prefix));
							FileWriter fw = new FileWriter(htmlFile, true);
							BufferedWriter conexao = new BufferedWriter(fw);
							conexao.write(str);
							conexao.newLine();
							conexao.close();
							// LOGGER.info(htmlFile.getPath());
							List<Element> p = HTMLWorker.parseToList(new FileReader(htmlFile.getPath()), styles);
							for (int k = 0; k < p.size(); ++k) {
								if (p.get(k) instanceof Paragraph) {
									Paragraph att = (Paragraph) p.get(k);
									// LOGGER.info("------->"+att.getContent());
									if (att.getContent().contains("||IMAGE||")) {
										String img = allMatches.poll();
										if (img != null 
												&& !img.substring(0, 100).contains("data:image/png;base64")
												&& !img.substring(0, 100).contains("data:image/jpg;base64")) {

											// LOGGER.info("IMG------->"+img);
											Image image = Image.getInstance(
													new URL(img.replaceAll("<img src=\"", "").replaceAll("\">", "").split("\"")[0]));
											float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
													- document.rightMargin()) / image.getWidth()) * 100;
											image.scalePercent(scaler * 0.4f);
											image.setAlignment(Element.ALIGN_CENTER);
											document.add(image);
											
										}else if(img != null ){
											try {
												img=img.replace("<img src=\"", "").replace("\">", "");
												final String base64Data = img.substring(img.indexOf(",") + 1);
												Image image = null;
												image = Image.getInstance(Base64.decode(base64Data));
		
												if (image != null) {
													float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
																- document.rightMargin()) / image.getWidth()) * 100;
													image.scalePercent(scaler * 0.4f);
													document.add(image);
												}
											}catch(Exception e) {
												LOGGER.error("Imagem não foi exportada no documento pdf.");
											}
										}
									} else {
										att.setFirstLineIndent(firstLineIndent);
										document.add(att);
									}
								} else if (p.get(k).getClass().getName().equals("com.itextpdf.text.List")) {
									com.itextpdf.text.List att = (com.itextpdf.text.List) p.get(k);
									att.setIndentationLeft(firstLineIndent);
									document.add(att);
								}
							}
							lastAttWasPlan = false;
							htmlFile.delete();
						}
					}
				}
			
			}

		}

		if (haveContent) {
			document.close();
		}
		outputDir.delete();
	}
	
	
	private int generateSummary(File finalSummaryPdfFile, TOCEvent event, int Npages) throws DocumentException, IOException {
		
		File outputDir;

		outputDir=tempFile();

		final String prefix = String.format("frisco-report-export-%d", System.currentTimeMillis());
		File summaryPdfFile = new File(outputDir, String.format("%s-summary.pdf", prefix));
		
		com.itextpdf.text.Document summaryDocument = new com.itextpdf.text.Document();
		PdfWriter.getInstance(summaryDocument, new FileOutputStream(summaryPdfFile));
		
		summaryDocument.setPageSize(PageSize.A4);
		// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
		summaryDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
		summaryDocument.open();

		Paragraph summaryTitle = new Paragraph("Sumário", titulo);
		summaryTitle.setLeading(interLineSpacing);
		summaryTitle.setSpacingAfter(paragraphSpacing);
		summaryTitle.setSpacingBefore(paragraphSpacing);
		summaryDocument.add(summaryTitle);

		Chunk dottedLine = new Chunk(new DottedLineSeparator());
		List<SimpleEntry<String, SimpleEntry<String, Integer>>> entries = event.getTOC();
		Paragraph p;
		int summaryCountPages = 0;
		for (SimpleEntry<String, SimpleEntry<String, Integer>> entry : entries) {
			// LOGGER.info(entry.getKey());
			Chunk chunk = new Chunk(entry.getKey(), titulo);
			SimpleEntry<String, Integer> value = entry.getValue();
			chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
			p = new Paragraph(chunk);
			p.add(dottedLine);
			chunk = new Chunk(String.valueOf(value.getValue()), titulo);
			chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
			p.add(chunk);
			summaryDocument.add(p);
		}
		summaryDocument.close();
		PdfReader summaryAux = new PdfReader(summaryPdfFile.getPath());

		summaryCountPages = summaryAux.getNumberOfPages() + Npages;
		
		summaryAux.close();


		com.itextpdf.text.Document finalSummaryDocument = new com.itextpdf.text.Document();
		PdfWriter.getInstance(finalSummaryDocument, new FileOutputStream(finalSummaryPdfFile));
		// Formato A4 do documento
		finalSummaryDocument.setPageSize(PageSize.A4);
		// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
		finalSummaryDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
		finalSummaryDocument.open();

		finalSummaryDocument.add(summaryTitle);
		for (SimpleEntry<String, SimpleEntry<String, Integer>> entry : entries) {
			// LOGGER.info(entry.getKey());
			Chunk chunk = new Chunk(entry.getKey(), titulo);
			SimpleEntry<String, Integer> value = entry.getValue();
			chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
			p = new Paragraph(chunk);
			p.add(dottedLine);
			chunk = new Chunk(String.valueOf(value.getValue() + summaryCountPages), titulo);
			chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
			p.add(chunk);
			finalSummaryDocument.add(p);
		}
		finalSummaryDocument.close();
		
		summaryPdfFile.delete();

		return summaryCountPages;
	}
	
	private File tempFile() throws IOException {
		File outputDir = File.createTempFile("frisco-document-export", ".pdf").getParentFile();
		//final String prefix = String.format("frisco-report-export-%d", System.currentTimeMillis());
		return outputDir;
	}
	
	
	/*riscos*/
	private void generateContent(File contentFile, String selecao, Long planId, TOCEvent event) throws DocumentException, IOException {
		
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(contentFile));
		
		writer.setPageEvent(event);
		
		File outputDir;
		/*final String storagePath =SystemConfigs.getConfig("store.pdfs");
		
		if (storagePath == null || storagePath.equals("") || storagePath.equals("${store.pdfs}")) {*/
			//outputDir = File.createTempFile("fprisco-document-export", ".pdf").getParentFile();
		outputDir=tempFile();
		/*}else {
			 outputDir = new File(storagePath);
		}*/
		final String prefix = String.format("frisco-report-export-%d", System.currentTimeMillis());

		
		
		String[] sections = null;
		if (selecao != null)
			sections = selecao.split(",");

		int secIndex = 0;
		int subSecIndex = 0;
		boolean lastAttWasPlan = false;
		boolean haveContent = false;

		document.open();
		
		document.setPageSize(PageSize.A4);
		document.newPage();
		
		haveContent=true;
		
		PlanRisk plan = this.policyBS.exists(planId,PlanRisk.class);
		if (GeneralUtils.isInvalid(plan)) {
			return;
		}	
		
		Policy policy = this.planriskBS.listPolicybyPlanRisk(plan);
		PaginatedList<RiskLevel> risk_level = this.policyBS.listRiskLevelbyPolicy(policy);
		PaginatedList<Unit> units= this.unitBS.listUnitsbyPlanRisk(plan);
		PaginatedList<Risk> risks = new PaginatedList<Risk>();
		
		List<Risk> list = new ArrayList<>();
		for(Unit unit: units.getList()) list.addAll(this.riskBS.listRiskbyUnit(unit).getList());
		risks.setList(list);
		risks.setTotal((long) list.size());
	
		PaginatedList<Incident> incidents= this.riskBS.listIncidentsbyRisk(risks);
		//PaginatedList<Monitor>

		
		/*
		Riscos - Crítico ameaças,
		Riscos - Muito Alto ameaças,
		Riscos - Crítico oportunidades,
		Riscos - Muito Alto oportunidades,
		*Incidentes - Ameaças,
		*Incidentes - Oportunidades,
		Riscos próximos a vencer,
		Riscos  em dia,
		Riscos atrasados,
		Riscos não iniciados
		*/
		

		//para cada item selecionado
		if(sections !=null) {
			for (int i = 0; i < sections.length; i++) {
				secIndex++;
				Chunk c = new Chunk(secIndex + ". " + sections[i], titulo);
				c.setGenericTag(secIndex + ". " + sections[i]);
				Paragraph secTitle = new Paragraph(c);
				document.add(secTitle);
				
				
				switch(sections[i]) {
					case "Incidentes - Ameaças": 
						for(Incident incident: incidents.getList()) {
							if(incident.getType() == 0) {
								Paragraph attTitle = new Paragraph(incident.getDescription(), titulo);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
							Paragraph attText = new Paragraph(incident.getAction(), texto);
							document.add(attText);
						}
						break;
						
					case "Incidentes - Oportunidades": 
						for(Incident incident: incidents.getList()) {
							if(incident.getType() == 1) {
								Paragraph attTitle = new Paragraph(incident.getDescription(), titulo);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
							Paragraph attText = new Paragraph(incident.getAction(), texto);
							document.add(attText);
						}
						break;
						
					case "Riscos próximos a vencer": 
						for(Risk risk : risks.getList()) {
							Monitor monitor = this.unitBS.lastMonitorbyRisk(risk);
							
							int state=this.unitBS.riskState(risk.getPeriodicity(),monitor.getBegin());
							if(state == 1) {
								Paragraph attTitle = new Paragraph(risk.getName(), titulo);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
						}
						
						break;
					case "Riscos em dia" : 
						for(Risk risk : risks.getList()) {
							Monitor monitor = this.unitBS.lastMonitorbyRisk(risk);
							
							int state=this.unitBS.riskState(risk.getPeriodicity(),monitor.getBegin());
							if(state == 2) {
								Paragraph attTitle = new Paragraph(risk.getName(), titulo);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
						}
						break;
						
					case "Riscos atrasados" : 
						for(Risk risk : risks.getList()) {
							Monitor monitor = this.unitBS.lastMonitorbyRisk(risk);
							
							int state=this.unitBS.riskState(risk.getPeriodicity(),monitor.getBegin());
							if(state == 3) {
								Paragraph attTitle = new Paragraph(risk.getName(), titulo);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
						}
						break;
						
					case "Riscos não iniciados"	:
						for(Risk risk : risks.getList()) {
							Monitor monitor = this.unitBS.lastMonitorbyRisk(risk);
							
							int state=this.unitBS.riskState(risk.getPeriodicity(),monitor.getBegin());
							if(state == 0) {
								Paragraph attTitle = new Paragraph(risk.getName(), titulo);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
						}
						break;
					
					default:
						
						for(RiskLevel rl :risk_level.getList()){
							if(sections[i].equals("Riscos - "+rl.getLevel()+" Ameaças")) {
								for(Risk risk : risks.getList()) {
									if(risk.getRiskLevel()==rl) {
										Paragraph attTitle = new Paragraph(risk.getName()
												+" | "+risk.getReason()
												+" | "+risk.getResult(), texto);
										
										
										attTitle.setLeading(interLineSpacing);
										attTitle.setSpacingAfter(paragraphSpacing);
										attTitle.setSpacingBefore(paragraphSpacing);
										document.add(attTitle);
									}
								}								
							}else if(sections[i].equals("Risco - "+rl.getLevel()+" Oportunidades")) {
								for(Risk risk : risks.getList()) {
									if(risk.getRiskLevel()==rl) {
										Paragraph attTitle = new Paragraph(risk.getName(), texto);
										attTitle.setLeading(interLineSpacing);
										attTitle.setSpacingAfter(paragraphSpacing);
										attTitle.setSpacingBefore(paragraphSpacing);
										document.add(attTitle);
									}
								}
							}
						}
				}

		
				/*
				Item item = this.itemBS.retrieveItembyId(Long.parseLong(sections[i]));//item altual
				PaginatedList<FieldItem> fielditens = this.itemBS.listFieldsByItem(item);//fields atual
				PaginatedList<SubItem> subs = this.itemBS.listSubItensByItem(item);	//lista todos subitens
				List <SubItem> actualsubitens= new ArrayList<SubItem>();	//lista de subitens selecionados
	
				
				haveContent = true;
				boolean secTitlePrinted = false;
				subSecIndex = 0;
				String secName =item.getName();
	
				if(fielditens.getTotal()>0){
					secIndex++;
				}
				
				for (FieldItem fielditem: fielditens.getList()) {
					
					if( fielditem.isText() && fielditem.getDescription() != null && !fielditem.getDescription().equals("")) {
						
						if (lastAttWasPlan) {
							document.setPageSize(PageSize.A4);
							document.newPage();
						}
						if (!secTitlePrinted) {
							Chunk c = new Chunk(secIndex + ". " + secName, titulo);
							c.setGenericTag(secIndex + ". " + secName);
							Paragraph secTitle = new Paragraph(c);
							secTitle.setLeading(interLineSpacing);
							secTitle.setSpacingAfter(paragraphSpacing);
							secTitle.setSpacingBefore(paragraphSpacing);
							document.add(secTitle);
							secTitlePrinted = true;
						}
						
						String attName = fielditem.getName();
						
						if (!attName.equals(secName)) {
							Paragraph attTitle = new Paragraph(attName, titulo);
							attTitle.setLeading(interLineSpacing);
							attTitle.setSpacingAfter(paragraphSpacing);
							attTitle.setSpacingBefore(paragraphSpacing);
							document.add(attTitle);
						}
						
						Map<String, String> pc2 = new HashMap<String, String>();
						pc2.put("line-height", "115%");
						pc2.put("margin-bottom", "6.0pt");
						pc2.put("text-align", "center");
						HashMap<String, String> spanc1 = new HashMap<String, String>();
						spanc1.put("text-justify", "inter-word");
	
						StyleSheet styles = new StyleSheet();
						styles.loadTagStyle("p", "text-indent", "1.25cm");
	
						String str = "<html>" + "<head>" + "</head><body style=\"text-indent: 1.25cm; \">"
								+ "<p style=\"text-indent: 1.25cm; \">";
							
						Queue<String> allMatches = new LinkedList<>();
						String value = fielditem.getDescription();
						if (fielditem.getDescription().contains("<img")) {
							Matcher m = Pattern.compile("<img [^>]*>").matcher(fielditem.getDescription());
							while (m.find()) {
								String match = m.group();
								allMatches.add(match);
								value = value.replace(match, "<p>||IMAGE||</p>");
							}
						}
						str += value + "</p></body></html>";
						
						File htmlFile = new File(outputDir, String.format("%s-1.html", prefix));
						FileOutputStream out = new FileOutputStream(htmlFile);
						out.write(str.getBytes());
						out.close();
	
						FileReader fr = new FileReader(htmlFile.getPath());
						
						List<Element> p = HTMLWorker.parseToList(fr, styles);
						
						fr.close();
						
						for (int k = 0; k < p.size(); ++k) {
							if (p.get(k) instanceof Paragraph) {
								Paragraph att = (Paragraph) p.get(k);
								// LOGGER.info("------->"+att.getContent());
								if (att.getContent().contains("||IMAGE||")) {
									String img = allMatches.poll();
									if (img != null) {
										// LOGGER.info("IMG------->"+img);
										Image image = Image.getInstance(
												new URL(img.replaceAll("<img src=\"", "").replaceAll("\">", "").split("\"")[0]));
										float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
												- document.rightMargin()) / image.getWidth()) * 100;
										image.scalePercent(scaler * 0.4f);
										image.setAlignment(Element.ALIGN_CENTER);
										document.add(image);
									}
								} else {
									att.setFirstLineIndent(firstLineIndent);
									document.add(att);
								}
							} else if (p.get(k).getClass().getName().equals("com.itextpdf.text.List")) {
								com.itextpdf.text.List att = (com.itextpdf.text.List) p.get(k);
								att.setIndentationLeft(firstLineIndent);
								document.add(att);
							}
						}
						lastAttWasPlan = false;
						htmlFile.delete();
					}
				}*/
				

			}
		}
			
		if (haveContent ) {
			document.close();
		}
	}

	
	/**
	 * Cria arquivo pdf  para exportar relatório  
	 * 
	 * 
	 * @param title
	 * @param author
	 * @param itemns
	 * @param subitens
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File exportReport(String title, String author,String itens, String subitens) throws IOException, DocumentException {

		File outputDir=tempFile();

		final String prefix = String.format("frisco-report-export-%d", System.currentTimeMillis());

		File finalSummaryPdfFile = new File(outputDir, String.format("%s-final-summary.pdf", prefix));
		File destinationFile = new File(outputDir, String.format("%s-mounted.pdf", prefix));
		File finalPdfFile = new File(outputDir, String.format("final-%s.pdf", prefix));
		File coverPdfFile = new File(outputDir, String.format("%s-cover.pdf", prefix));
		File contentFile = new File(outputDir, String.format("%s-content.pdf", prefix));		

		generateCover(coverPdfFile, title, author);

		TOCEvent event = new TOCEvent();
		PdfReader cover = new PdfReader(coverPdfFile.getPath());
		
		generateContent(contentFile, itens, subitens, event);
		
		int summaryCountPages = generateSummary( finalSummaryPdfFile, event, cover.getNumberOfPages());		

		com.itextpdf.text.Document newDocument = new com.itextpdf.text.Document();

		PdfImportedPage page;
		int n;
		PdfCopy copy = new PdfCopy(newDocument, new FileOutputStream(destinationFile.getPath()));
		newDocument.open();

		PdfReader summary = new PdfReader(finalSummaryPdfFile.getPath());
		PdfReader content;
		
		// CAPA
		n = cover.getNumberOfPages();
			for (int i = 0; i < n;) {
			page = copy.getImportedPage(cover, ++i);
			copy.addPage(page);
		}

		// SUMÁRIO
		n = summary.getNumberOfPages();
		for (int i = 0; i < n;) {
			page = copy.getImportedPage(summary, ++i);
			copy.addPage(page);
		}
			
		if(contentFile.length()>0) {
			content = new PdfReader(contentFile.getPath());
			// CONTEÚDO
			n = content.getNumberOfPages();
			for (int i = 0; i < n;) {
				page = copy.getImportedPage(content, ++i);
				copy.addPage(page);
			}
			content.close();
		}
			
		cover.close();
		summary.close();
		newDocument.close();

		manipulatePdf(destinationFile.getPath(), finalPdfFile.getPath(), newDocument, summaryCountPages);
		
		/*destinationFile.delete();
		coverPdfFile.delete();
		finalSummaryPdfFile.delete();
		contentFile.delete();	
		outputDir.delete();*/
		
		for (File f : outputDir.listFiles()) {
		    if (f.getName().startsWith("frisco-") 
		    		&& (f.getName().endsWith(".pdf") || f.getName().endsWith(".html"))) {
		        f.delete();
		    }
		}	
		return finalPdfFile; //capa+sumario+conteudo+paginação
		
	}


	public File exportReport(String title, String author, String selecao, Long planId) throws IOException, DocumentException {
		
		File outputDir=tempFile();
		
		final String prefix = String.format("frisco-report-export-%d", System.currentTimeMillis());

		File finalSummaryPdfFile = new File(outputDir, String.format("%s-final-summary.pdf", prefix));
		File destinationFile = new File(outputDir, String.format("%s-mounted.pdf", prefix));
		File finalPdfFile = new File(outputDir, String.format("%s-final.pdf", prefix));
		File coverPdfFile = new File(outputDir, String.format("%s-cover.pdf", prefix));
		File contentFile = new File(outputDir, String.format("%s-content.pdf", prefix));		

		generateCover(coverPdfFile, title, author);

		TOCEvent event = new TOCEvent();
		PdfReader cover = new PdfReader(coverPdfFile.getPath());

		generateContent(contentFile, selecao, planId, event);
		
		int summaryCountPages = generateSummary( finalSummaryPdfFile, event, cover.getNumberOfPages());		
		

		com.itextpdf.text.Document newDocument = new com.itextpdf.text.Document();

		PdfImportedPage page;
		int n;
		PdfCopy copy = new PdfCopy(newDocument, new FileOutputStream(destinationFile.getPath()));
		newDocument.open();

		PdfReader summary = new PdfReader(finalSummaryPdfFile.getPath());
		PdfReader content;

		// CAPA
		n = cover.getNumberOfPages();
		for (int i = 0; i < n;) {
			page = copy.getImportedPage(cover, ++i);
			copy.addPage(page);
		}

		// SUMÁRIO
		n = summary.getNumberOfPages();
		for (int i = 0; i < n;) {
			page = copy.getImportedPage(summary, ++i);
			copy.addPage(page);
		}
		
		if(contentFile.length()>0) {
			content = new PdfReader(contentFile.getPath());
			// CONTEÚDO
			n = content.getNumberOfPages();
			for (int i = 0; i < n;) {
				page = copy.getImportedPage(content, ++i);
				copy.addPage(page);
			}
			content.close();
		}
			
		cover.close();
		summary.close();		
		newDocument.close();

		manipulatePdf(destinationFile.getPath(), finalPdfFile.getPath(), newDocument, summaryCountPages);
		
		destinationFile.delete();
		coverPdfFile.delete();
		finalSummaryPdfFile.delete();
		contentFile.delete();	
		
		outputDir.delete();
	
		return finalPdfFile;  //capa+sumario+conteudo+paginação
	}

	public File exportUnitReport(String title, String author, String selecao, Long planId) throws IOException, DocumentException {
		
		File outputDir=tempFile();
		
		final String prefix = String.format("frisco-report-export-%d", System.currentTimeMillis());

		File finalSummaryPdfFile = new File(outputDir, String.format("%s-final-summary.pdf", prefix));
		File destinationFile = new File(outputDir, String.format("%s-mounted.pdf", prefix));
		File finalPdfFile = new File(outputDir, String.format("%s-final.pdf", prefix));
		File coverPdfFile = new File(outputDir, String.format("%s-cover.pdf", prefix));
		File contentFile = new File(outputDir, String.format("%s-content.pdf", prefix));		

		generateCover(coverPdfFile, title, author);

		TOCEvent event = new TOCEvent();
		PdfReader cover = new PdfReader(coverPdfFile.getPath());

		generateContent(contentFile, selecao, planId, event);
		
		int summaryCountPages = generateSummary( finalSummaryPdfFile, event, cover.getNumberOfPages());		
		

		com.itextpdf.text.Document newDocument = new com.itextpdf.text.Document();

		PdfImportedPage page;
		int n;
		PdfCopy copy = new PdfCopy(newDocument, new FileOutputStream(destinationFile.getPath()));
		newDocument.open();

		PdfReader summary = new PdfReader(finalSummaryPdfFile.getPath());
		PdfReader content;

		// CAPA
		n = cover.getNumberOfPages();
		for (int i = 0; i < n;) {
			page = copy.getImportedPage(cover, ++i);
			copy.addPage(page);
		}

		// SUMÁRIO
		n = summary.getNumberOfPages();
		for (int i = 0; i < n;) {
			page = copy.getImportedPage(summary, ++i);
			copy.addPage(page);
		}
		
		if(contentFile.length()>0) {
			content = new PdfReader(contentFile.getPath());
			// CONTEÚDO
			n = content.getNumberOfPages();
			for (int i = 0; i < n;) {
				page = copy.getImportedPage(content, ++i);
				copy.addPage(page);
			}
			content.close();
		}
			
		cover.close();
		summary.close();		
		newDocument.close();

		manipulatePdf(destinationFile.getPath(), finalPdfFile.getPath(), newDocument, summaryCountPages);
		
		destinationFile.delete();
		coverPdfFile.delete();
		finalSummaryPdfFile.delete();
		contentFile.delete();	
		
		outputDir.delete();
	
		return finalPdfFile;  //capa+sumario+conteudo+paginação
	}


}